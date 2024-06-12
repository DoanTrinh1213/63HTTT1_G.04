package space.app.Activity;

import static android.app.PendingIntent.getActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import space.app.Adapter.WriteReviewAdapter;
import space.app.R;

public class RewriteActivity extends AppCompatActivity implements WriteReviewAdapter.CountOfImagesWhenRemoved, WriteReviewAdapter.itemClickListener {

    RecyclerView recyclerView;
    FloatingActionButton pick, deletepick;
    ArrayList<Uri> uri = new ArrayList<>();

    private static final int READ_PERMISSION = 101;
    private static final int PICK_IMAGE = 1;
    ActivityResultLauncher<Intent> activityResultLauncher;
    WriteReviewAdapter adapter;
    private Uri imageuri;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_write_review);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.WriteReview), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewGalleryImages);
        pick = findViewById(R.id.pick);
        deletepick = findViewById(R.id.deletepick);

        adapter = new WriteReviewAdapter(uri, getApplicationContext(), this, this);
        recyclerView.setLayoutManager(new GridLayoutManager(RewriteActivity.this, 4));
        recyclerView.setAdapter(adapter);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == RESULT_OK && null != result.getData()) {
                            if (result.getData().getClipData() != null) {
                                // nhiều hình ảnh
                                int countOfImages = result.getData().getClipData().getItemCount();
                                for (int i = 0; i < countOfImages; i++) {
                                    // gioi han anh
                                    if (uri.size() < 11) {
                                        imageuri = result.getData().getClipData().getItemAt(i).getUri();
                                        uri.add(imageuri);
                                        uploadToFirebase();
                                    } else {
                                        Toast.makeText(RewriteActivity.this, "Không cho phép chọn quá 11 tấm hình", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                // thong bao adapter
                                adapter.notifyDataSetChanged();
                            } else {
                                // sắp xếp không có hình ảnh nào được lấy từ dow ngay cả khi một hình ảnh được chọn
                                if (uri.size() < 11) {
                                    // chon 1 anh
                                    imageuri = result.getData().getData();
                                    // them vao mang
                                    uri.add(imageuri);
                                    uploadToFirebase();
                                } else {
                                    Toast.makeText(RewriteActivity.this, "Không cho phép chọn quá 11 tấm hình",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            // nguoi dung ko chon anh
                            Toast.makeText(RewriteActivity.this, "Bạn không chọn tấm hình nào",
                                    Toast.LENGTH_SHORT).show();
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
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                // intent.setAction(Intent.ACTION_GET_CONTENT);
                // startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                activityResultLauncher.launch(intent);
            }
        });

        deletepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri.clear();
                adapter.notifyDataSetChanged();
                Toast.makeText(v.getContext(), "Bạn đã xóa tất cả ảnh", Toast.LENGTH_SHORT).show();
            }
        });


    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
//            if (data.getClipData() != null) {
//                // nhiều hình ảnh
//                int countOfImages = data.getClipData().getItemCount();
//                for (int i = 0; i < countOfImages; i++) {
//                    // gioi han anh
//                    if (uri.size() < 11) {
//                        imageuri = data.getClipData().getItemAt(i).getUri();
//                        uri.add(imageuri);
//                        uploadToFirebase();
//                    } else {
//                        Toast.makeText(RewriteActivity.this, "Không cho phép chọn quá 11 tấm hình", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//                // thong bao adapter
//                adapter.notifyDataSetChanged();
//            } else {
//                // sắp xếp không có hình ảnh nào được lấy từ dow ngay cả khi một hình ảnh được chọn
//                if (uri.size() < 11) {
//                    // chon 1 anh
//                    imageuri = data.getData();
//                    // them vao mang
//                    uri.add(imageuri);
//                    uploadToFirebase();
//                } else {
//                    Toast.makeText(RewriteActivity.this, "Không cho phép chọn quá 11 tấm hình", Toast.LENGTH_SHORT).show();
//                }
//            }
//            adapter.notifyDataSetChanged();
//        } else {
//            // nguoi dung ko chon anh
//            Toast.makeText(this, "Bạn không chọn tấm hình nào", Toast.LENGTH_SHORT).show();
//        }
//
//
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
            }
        }
    }

    @Override
    public void clicked(int getSize) {
        Toast.makeText(this, "Bạn đã xóa 1 ảnh", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClick(int position) {
        // zoom anh
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


    private void uploadToFirebase() {


        final String randomName = UUID.randomUUID().toString();
        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + randomName);
        storageReference.putFile(imageuri)
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
}
