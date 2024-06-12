package space.app.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import space.app.Adapter.CustomSpinnerAdapter;
import space.app.Adapter.CafeImageRecyclerViewAdapter;
import space.app.Adapter.MenuImageRecyclerViewAdapter;
import space.app.Model.Cafe;
import space.app.R;
import space.app.UI.Fragment.FragmentMe;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class ContributeCafeInformationActivity extends AppCompatActivity implements CafeImageRecyclerViewAdapter.OnImageRemovedListener, MenuImageRecyclerViewAdapter.OnImageRemovedListener {
    private ImageView BackPerson;
    private RecyclerView recyclerViewCafe;
    private RecyclerView recyclerViewMenu;
    private FloatingActionButton FloatingActionButtonCafe;
    private FloatingActionButton FloatingActionButtonMenu;

    private ArrayList<Uri> ChooseImageListCafe;
    private ArrayList<Uri> ChooseImageListMenu;
    private static final int MIN_IMAGE_COUNT = 3;

    private static final int PERMISSION_REQUEST_CODE = 2;
    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private EditText edtMap, edtContact, edtDescription, edtTime, edtPrice, edtNameCafe, edtAddress;
    private MaterialButton btnSendContributeCafe;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute_cafe_information);

        // Back to FragmentMe
        BackPerson = findViewById(R.id.BackPerson);
        BackPerson.setOnClickListener(v -> {
            BackPerson.setVisibility(View.GONE);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container_Me, new FragmentMe()).addToBackStack(null).commit();
        });

        // Spinner
        Spinner spinner = findViewById(R.id.spinner);
        String[] items = {"1 mình", "Bar/Pub", "Bạn bè", "Hẹn hò", "Làm việc", "Mở muộn", "Sống ảo", "Đọc sách", "Đồ uống ngon"};

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Initialize image picker Cafe
        recyclerViewCafe = findViewById(R.id.recyclerViewCafe);
        recyclerViewMenu = findViewById(R.id.recyclerViewMenu);
        ChooseImageListCafe = new ArrayList<>();
        ChooseImageListMenu = new ArrayList<>();
        FloatingActionButtonCafe = findViewById(R.id.floatingActionButtonCafe);
        FloatingActionButtonMenu = findViewById(R.id.floatingActionButtonMenu);
        recyclerViewCafe.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        FloatingActionButtonCafe.setOnClickListener(v -> checkPermissionAndPickImageForCafe(PICK_IMAGE_REQUEST_CODE));
        FloatingActionButtonMenu.setOnClickListener(v -> checkPermissionAndPickImageForMenu(PICK_IMAGE_REQUEST_CODE + 1));



        // InformationCafe
        edtMap = findViewById(R.id.edtMap);
        edtContact = findViewById(R.id.edtContact);
        edtDescription = findViewById(R.id.edtDescription);
        edtTime = findViewById(R.id.edtTime);
        edtPrice = findViewById(R.id.edtPrice);
        edtNameCafe = findViewById(R.id.edtNameCafe);
        edtAddress = findViewById(R.id.edtAddress);
        btnSendContributeCafe = findViewById(R.id.btnSendContributeCafe);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Cafe");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("Cafe");
        btnSendContributeCafe.setOnClickListener(v -> {
            if (ChooseImageListCafe.size() < MIN_IMAGE_COUNT || ChooseImageListMenu.size() < MIN_IMAGE_COUNT) {
                Toast.makeText(this, "Bạn cần up tối thiểu 3 ảnh", Toast.LENGTH_SHORT).show();
                return;
            }
            uploadImagesAndSaveCafe();
        });
    }

    private void uploadImagesAndSaveCafe() {
        String link = edtMap.getText().toString();
        String resName = edtNameCafe.getText().toString();
        String address = edtAddress.getText().toString();
        String purpose = ((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString();
        String timeOpen = edtTime.getText().toString();
        String describe = edtDescription.getText().toString();
        String price = edtPrice.getText().toString();
        String contact = edtContact.getText().toString();

        if (ChooseImageListCafe.size() < MIN_IMAGE_COUNT || ChooseImageListMenu.size() < MIN_IMAGE_COUNT) {
            Toast.makeText(this, "Bạn cần up tối thiểu 3 ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thực hiện tải ảnh lên Firebase Storage và lưu thông tin quán vào Firebase Database
        uploadImagesAndSaveCafeInfo(link, resName, address, purpose, timeOpen, describe, price, contact);
    }

    private void uploadImagesAndSaveCafeInfo(String link, String resName, String address, String purpose, String timeOpen, String describe, String price, String contact) {
        DatabaseReference cafeRef = FirebaseDatabase.getInstance().getReference().child("Cafe").push();
        String cafeId = cafeRef.getKey();

        Cafe cafe = new Cafe(cafeId, resName, address, describe, Float.parseFloat(price), timeOpen, contact, "", link, purpose, "");

        // Lưu URL của ảnh quán vào Firebase Storage và cập nhật vào đối tượng Cafe
        uploadImages(ChooseImageListCafe, "CafeImages", new UploadImagesCallback() {
            @Override
            public void onImagesUploaded(ArrayList<String> imageUrls) {
                cafe.setCafeImageUrls(imageUrls);

                // Lưu thông tin quán vào Firebase Database
                cafeRef.setValue(cafe)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(ContributeCafeInformationActivity.this, "Thông tin quán đã được lưu thành công!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(ContributeCafeInformationActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        uploadImages(ChooseImageListMenu, "MenuImages", new UploadImagesCallback() {
            @Override
            public void onImagesUploaded(ArrayList<String> imageUrls) {
                cafe.setMenuImageUrls(imageUrls);

                cafeRef.child("menuImageUrls").setValue(imageUrls);
            }
        });
    }

    private void uploadImages(ArrayList<Uri> imageUris, String folderName, UploadImagesCallback callback) {
        ArrayList<String> imageUrls = new ArrayList<>();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(folderName);

        for (Uri imageUri : imageUris) {
            StorageReference imageRef = storageRef.child(imageUri.getLastPathSegment());
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Lấy URL của ảnh đã tải lên
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Thêm URL vào danh sách
                            imageUrls.add(uri.toString());

                            // Kiểm tra nếu đã tải lên hết ảnh
                            if (imageUrls.size() == imageUris.size()) {
                                // Gọi callback để trả về danh sách URL của ảnh
                                callback.onImagesUploaded(imageUrls);
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ContributeCafeInformationActivity.this, "Lỗi khi tải ảnh lên: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    interface UploadImagesCallback {
        void onImagesUploaded(ArrayList<String> imageUrls);
    }



    private void checkPermissionAndPickImageForCafe(int pickImageRequestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ContributeCafeInformationActivity.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ContributeCafeInformationActivity.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                pickImageGalleryForCafe(pickImageRequestCode);
            }
        } else {
            pickImageGalleryForCafe(pickImageRequestCode);
        }
    }
    private void pickImageGalleryForCafe(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCode);
    }


    private void checkPermissionAndPickImageForMenu(int i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ContributeCafeInformationActivity.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ContributeCafeInformationActivity.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                pickImageGalleryForMenu();
            }
        } else {
            pickImageGalleryForMenu();
        }
    }

    private void pickImageGalleryForMenu() {
    }

    private void pickImageGalleryForMenu(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            ArrayList<Uri> selectedImages = new ArrayList<>();
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    selectedImages.add(imageUri);
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                selectedImages.add(imageUri);
            }

            if (selectedImages.size() < MIN_IMAGE_COUNT) {
                Toast.makeText(this, "Bạn cần up tối thiểu 3 ảnh", Toast.LENGTH_SHORT).show();
            } else {
                if (requestCode == PICK_IMAGE_REQUEST_CODE) {
                    ChooseImageListCafe.clear();
                    ChooseImageListCafe.addAll(selectedImages);
                    setAdapter(ChooseImageListCafe, requestCode);
                } else if (requestCode == PICK_IMAGE_REQUEST_CODE + 1) {
                    ChooseImageListMenu.clear();
                    ChooseImageListMenu.addAll(selectedImages);
                    setAdapter(ChooseImageListMenu, requestCode);
                }
            }
        }
    }

    private void setAdapter(ArrayList<Uri> imageList, int requestCode) {
        RecyclerView.Adapter cafeAdapter;
        if (requestCode == PICK_IMAGE_REQUEST_CODE) {
            cafeAdapter = new CafeImageRecyclerViewAdapter(this, ChooseImageListCafe, this);
            recyclerViewCafe.setAdapter(cafeAdapter);
        } else if (requestCode == PICK_IMAGE_REQUEST_CODE + 1) {
            MenuImageRecyclerViewAdapter menuAdapter = new MenuImageRecyclerViewAdapter(this, ChooseImageListMenu, this);
            recyclerViewMenu.setAdapter(menuAdapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageGalleryForCafe(requestCode);
                pickImageGalleryForMenu(requestCode);
            } else {
                pickImageGalleryForCafe(requestCode);
                pickImageGalleryForMenu(requestCode);

            }
        }
    }



    @Override
    public void onImageRemoved() {
        // Handle image removal, if needed
    }

}