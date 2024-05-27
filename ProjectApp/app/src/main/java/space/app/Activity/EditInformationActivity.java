package space.app.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import kotlin.Unit;
import space.app.R;
import space.app.UI.Fragment.FragmentMe;

public class EditInformationActivity extends AppCompatActivity {
    private ImageView iconBackPerson;
    ImageView imageView;
    MaterialButton materialButton;

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
//                iconBackPerson.setVisibility(View.GONE);
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container_Me,new FragmentMe()).addToBackStack(null).commit();

            }
        });
        materialButton = findViewById(R.id.imagePersonChange);
        imageView = findViewById(R.id.imagePerson);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String imageUrl = sharedPreferences.getString("imageUrl", null);
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).into(imageView);
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


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            imageView.setImageURI(uri);
            uploadImageToFirebase(uri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(EditInformationActivity.this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(EditInformationActivity.this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToFirebase(Uri uri) {
        if (uri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();

            String uniqueID = UUID.randomUUID().toString();
            StorageReference imageRef = storageReference.child("images/" + uniqueID);

            UploadTask uploadTask = imageRef.putFile(uri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    Toast.makeText(EditInformationActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    saveImageUrl(downloadUri.toString());
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(EditInformationActivity.this, "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void saveImageUrl(String url) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("imageUrl", url);
        editor.apply();
    }
}