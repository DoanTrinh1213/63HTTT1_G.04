package space.app.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import space.app.Adapter.CustomSpinnerAdapter;
import space.app.Adapter.ImageRecyclerViewAdapter;
import space.app.Database.Entity.UserEntity;
import space.app.Model.User;
import space.app.R;
import space.app.Repository.UserRepository;
import space.app.UI.Fragment.FragmentMe;

public class ContributeCafeInformationActivity extends AppCompatActivity {
    private ImageView BackPerson;
    private RelativeLayout PickImageCafe;
    private RecyclerView recyclerView;
    private FloatingActionButton FloatingActionButtonCafe;

    private ArrayList<Uri> ChooseImageList;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private static final int PICK_IMAGE_REQUEST_CODE = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute_cafe_information);

        // back fragmentMe
        BackPerson = findViewById(R.id.BackPerson);
        BackPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackPerson.setVisibility(View.GONE);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_Me, new FragmentMe()).addToBackStack(null).commit();
            }
        });


        // Spinner
        Spinner spinner = findViewById(R.id.spinner);
        String[] items = {"1 mình", "Bar/Pub", "Bạn bè", "Hẹn hò", "Làm việc", "Mở muộn", "Sống ảo", "Đọc sách", "Đồ uống ngon"};

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Initialize image picker
        PickImageCafe = findViewById(R.id.PickImageCafe);
        recyclerView = findViewById(R.id.recyclerView);
        ChooseImageList = new ArrayList<>();
        FloatingActionButtonCafe = findViewById(R.id.floatingActionButtonCafe);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        FloatingActionButtonCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndPickImage();
            }
        });
    }

    private void checkPermissionAndPickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ContributeCafeInformationActivity.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ContributeCafeInformationActivity.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                pickImageCafeGallery();
            }
        } else {
            pickImageCafeGallery();
        }
    }

    private void pickImageCafeGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    ChooseImageList.add(imageUri);
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                ChooseImageList.add(imageUri);
            }
            setAdapter();
        }
    }

    private void setAdapter() {
        ImageRecyclerViewAdapter adapter = new ImageRecyclerViewAdapter(this, ChooseImageList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageCafeGallery();
            } else {
                pickImageCafeGallery();

            }
        }
    }
}
