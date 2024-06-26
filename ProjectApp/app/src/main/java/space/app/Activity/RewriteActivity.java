package space.app.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import space.app.Adapter.WriteReviewAdapter;
import space.app.Model.Cafe;
import space.app.Model.Post;
import space.app.R;

public class RewriteActivity extends AppCompatActivity implements WriteReviewAdapter.CountOfImagesWhenRemoved, WriteReviewAdapter.itemClickListener {

    private static final int READ_PERMISSION = 101;
    private RecyclerView recyclerView;
    private FloatingActionButton pick, deletepick;
    private ArrayList<Uri> uri = new ArrayList<>();
    private RatingBar ratingBar;
    private EditText editTextExperience;
    private Button btnExperience;
    private ImageView iconClose;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private WriteReviewAdapter adapter;
    private Uri imageuri;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private Cafe cafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_write_review);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.WriteReview), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        cafe = (Cafe) intent.getSerializableExtra("rewriteCafe");

        iconClose = findViewById(R.id.iconClose);
        recyclerView = findViewById(R.id.recyclerViewGalleryImages);
        pick = findViewById(R.id.pick);
        deletepick = findViewById(R.id.deletepick);
        ratingBar = findViewById(R.id.ratingBar);
        editTextExperience = findViewById(R.id.editTextExperience);
        btnExperience = findViewById(R.id.btnExperience);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("Post");

        adapter = new WriteReviewAdapter(uri, getApplicationContext(), this, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(adapter);

        iconClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            if (result.getData().getClipData() != null) {
                                int countOfImages = result.getData().getClipData().getItemCount();
                                for (int i = 0; i < countOfImages; i++) {
                                    if (uri.size() < 5) {
                                        imageuri = result.getData().getClipData().getItemAt(i).getUri();
                                        uri.add(imageuri);
                                    } else {
                                        Toast.makeText(RewriteActivity.this, "Không cho phép chọn quá 5 tấm hình", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else if (result.getData().getData() != null) {
                                if (uri.size() < 5) {
                                    imageuri = result.getData().getData();
                                    uri.add(imageuri);
                                } else {
                                    Toast.makeText(RewriteActivity.this, "Không cho phép chọn quá 5 tấm hình", Toast.LENGTH_SHORT).show();
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(RewriteActivity.this, "Bạn không chọn tấm hình nào", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(RewriteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RewriteActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
                } else {
                    openImagePicker();
                }
            }
        });


        deletepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri.clear();
                adapter.notifyDataSetChanged();
                Toast.makeText(RewriteActivity.this, "Bạn đã xóa tất cả ảnh", Toast.LENGTH_SHORT).show();
            }
        });

        btnExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        activityResultLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Quyền truy cập bị từ chối", Toast.LENGTH_SHORT).show();
            }
            openImagePicker();
        }
    }

    @Override
    public void clicked(int getSize) {
        Toast.makeText(this, "Bạn đã xóa 1 ảnh", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClick(int position) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_zoom);

        ImageView imageView = dialog.findViewById(R.id.imageViewDialog);
        Button btnClose = dialog.findViewById(R.id.btnCloseDiaolog);

        imageView.setImageURI(uri.get(position));

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void uploadToFirebase(Uri imageUri) {
        final String randomName = UUID.randomUUID().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String idUser = sharedPreferences.getString("idUser", null);
        storageReference = firebaseStorage.getReference().child("Img/restaurents/" + cafe.getIdCafe() + "/comments/" + idUser + "/" + randomName);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(RewriteActivity.this, "Ảnh đã tải lên", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RewriteActivity.this, "Tải ảnh lên không thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addComment() {
        String comment = editTextExperience.getText().toString().trim();
        float starRating = ratingBar.getRating();

        if (comment.isEmpty() || starRating == 0.0f) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String idUser = sharedPreferences.getString("idUser", null);
        DatabaseReference postRef = firebaseDatabase.getReference().child("Post").push();
        String id = postRef.getKey();

        for(Uri url : uri){
            uploadToFirebase(url);
        }
        String imagesCommentsPath="";
        if(!uri.isEmpty()){
            imagesCommentsPath = "Img/restaurents/" + cafe.getIdCafe() + "/comments/" + idUser;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date currentDate = new Date();
        String formattedDate = sdf.format(currentDate);

        Post post = new Post(id, cafe.getIdCafe(), idUser, comment,formattedDate, imagesCommentsPath, String.valueOf(starRating));

        postRef.setValue(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RewriteActivity.this, "Cảm ơn ý kiến của bạn nhé <3<3", Toast.LENGTH_SHORT).show();
                        editTextExperience.setText("");
                        ratingBar.setRating(0);
                        uri.clear();
                        adapter.notifyDataSetChanged();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RewriteActivity.this, "Lỗi khi thêm đánh giá: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
