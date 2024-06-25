package space.app.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.UUID;

import kotlin.Unit;
import space.app.Helper.Utils;
import space.app.R;
import space.app.UI.Fragment.FragmentMe;
import space.app.UI.Fragment.FragmentPerson;

public class EditInformationActivity extends AppCompatActivity {
    private ImageView iconBackPerson;
    ImageView userImage;
    MaterialButton materialButton;
    EditText edtUserName;
    EditText edtDescription;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    FirebaseStorage storage;
    MaterialButton saveButton;

    Uri uriImage;
    private AlertDialog alertDialog;


    private static final int IMAGE_PICKER_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_information);

        iconBackPerson = findViewById(R.id.iconBackPerson);
        iconBackPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        materialButton = findViewById(R.id.imagePersonChange);
        userImage = findViewById(R.id.imagePerson);
        saveButton = findViewById(R.id.SendEditInformation);
        edtUserName = findViewById(R.id.edtUserName);
        edtDescription = findViewById(R.id.edtDescription);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String idUser =Utils.hashEmail(mAuth.getCurrentUser().getEmail());;

        if(mAuth.getCurrentUser().getEmail().equalsIgnoreCase("findCoffee2003@gmail.com"))
            idUser ="findCoffee";

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("userName", null);
        String description = sharedPreferences.getString("description", null);
        String imageUrl = sharedPreferences.getString("imageUrl", null);


//        userImage.setImageURI(Uri.parse(imageUrl));

        edtUserName.setText(username);
        edtDescription.setText(description);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            userImage.setImageURI(Uri.parse(imageUrl));
        } else {
            userImage.setImageResource(R.drawable.logo);
//            Toast.makeText(EditInformationActivity.this, "Không thể tải ảnh , vui lòng thêm lại ảnh hoặc kiểm tra kết nối mạng và thử lại!", Toast.LENGTH_SHORT).show();
        }
        materialButton.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .cropSquare()
                    .compress(1000)
                    .maxResultSize(1000, 1000)
                    .createIntent(intent -> {
                        startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE);
                        return Unit.INSTANCE;
                    });
        });
        String finalIdUser = idUser;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edtUserName.getText().toString().isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditInformationActivity.this);
                    LayoutInflater layoutInflater = getLayoutInflater();
                    View view = layoutInflater.inflate(R.layout.loading_compo,null);
                    TextView textView = view.findViewById(R.id.nameOfLoading);
                    textView.setText("Đang tải lên! Vui lòng chờ ...");
                    builder.setView(view);
                    builder.setCancelable(false);
                    alertDialog = builder.create();
                    alertDialog.show();


                    firebaseDatabase.getReference("User").child(finalIdUser).child("userName").setValue(edtUserName.getText().toString());
                    firebaseDatabase.getReference("User").child(finalIdUser).child("describe").setValue(edtDescription.getText().toString());
                    if (uriImage != null) {
                        uploadImageToFirebase(uriImage);
                    }
                    else{
                        Toast toast = new Toast(EditInformationActivity.this);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.setText("Lưu thành công!");
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.show();

                        Intent intent = new Intent(EditInformationActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast toast = new Toast(EditInformationActivity.this);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.setText("Username không thể để trống!");
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            Log.d("URI image", uri.toString());
            userImage.setImageURI(uri);
            uriImage = uri;
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(EditInformationActivity.this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(EditInformationActivity.this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }


    private void uploadImageToFirebase(Uri uri) {
        if (uri != null) {
            storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();

            String uniqueID = UUID.randomUUID().toString();
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            String email = firebaseAuth.getCurrentUser().getEmail();
            String idUser = Utils.hashEmail(email);
            if(email.equalsIgnoreCase("findCoffee2003@gmail.com"))
                idUser="findCoffee";
            deleteImageFromFirebase(idUser);
            StorageReference imageRef = storageReference.child("Img/users/" + idUser + "/" + uniqueID);
            UploadTask uploadTask = imageRef.putFile(uri);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    deleteImageFromDevice(uri);
                    saveImageUrl(downloadUri.toString());

                    Toast.makeText(EditInformationActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "Lưu thành công!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(EditInformationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(EditInformationActivity.this, "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void saveImageUrl(String url) {
        alertDialog.dismiss();


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.d("Uri", url);
        editor.putString("imageUrl", url);
        editor.apply();
        String imageUrl = sharedPreferences.getString("imageUrl", null);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String email = firebaseAuth.getCurrentUser().getEmail();
        String idUser = Utils.hashEmail(email);
        if(email.equalsIgnoreCase("findCoffee2003@gmail.com"))
            idUser ="findCoffee";
        String id = Utils.hash32b(idUser);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference("User").child(idUser).child("image").setValue(url);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url);
            File directory = new File(getFilesDir(), "userImage");
            if (!directory.exists()) {
                directory.mkdir();
            }
            String fileName = id + ".jpg";
            File[] files = directory.listFiles();
            if (files != null) {
                for (File existingFile : files) {
                    existingFile.delete();
                }
            }
            File file = null;
            file = new File(directory, fileName);
            File finalFile = file;
            storageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Uri fileUri = Uri.fromFile(finalFile);
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("imageUrl", fileUri.toString());
                    editor.apply();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EditInformationActivity.this, "Đã cập nhật ảnh thành công!", Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            View view = LayoutInflater.from(getApplication()).inflate(R.layout.fragment_person, null);
                            ImageView imageview = view.findViewById(R.id.userImage);
                            imageview.setImageURI(Uri.parse(sharedPreferences.getString("imageUrl", null)));

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Xử lý trường hợp tải ảnh không thành công
                    Log.e("Firebase", "Error downloading image: " + e.getMessage());
                }
            });
        } else {
            Toast.makeText(EditInformationActivity.this, "Không thể tải ảnh , vui lòng thêm lại ảnh hoặc kiểm tra kết nối mạng và thử lại!", Toast.LENGTH_SHORT);
        }
    }

    private void deleteImageFromDevice(Uri uri) {
        File file = new File(uri.getPath());
        if (file.exists()) {
            if (file.delete()) {
                Log.d("Delete Image", "Image deleted successfully: " + uri.getPath());
            } else {
                Log.d("Delete Image", "Failed to delete image: " + uri.getPath());
            }
        }
    }

    private void deleteImageFromFirebase(String idUser) {
        storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference userImageRef = storageReference.child("Img/users/" + idUser);
        userImageRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                item.delete().addOnSuccessListener(aVoid -> {
                    // Xóa thành công, thực hiện các hành động tiếp theo nếu cần
                }).addOnFailureListener(exception -> {
                    // Xảy ra lỗi khi xóa ảnh
                    Log.e("Firebase", "Error deleting image: ", exception);
                });
            }
        }).addOnFailureListener(exception -> {
            // Xảy ra lỗi khi liệt kê các tệp trong thư mục
            Log.e("Firebase", "Error listing images: ", exception);
        });
    }
}