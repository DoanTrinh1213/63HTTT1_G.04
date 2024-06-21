package space.app.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import space.app.Adapter.CustomSpinnerAdapter;
import space.app.Database.Entity.CafeEntity;
import space.app.Database.Entity.UserEntity;
import space.app.Helper.Utils;
import space.app.Model.User;
import space.app.Adapter.CafeImageRecyclerViewAdapter;
import space.app.Adapter.MenuImageRecyclerViewAdapter;
import space.app.Model.Cafe;
import space.app.R;
import space.app.Repository.UserRepository;
import space.app.UI.Fragment.FragmentMe;
import space.app.ViewModel.CafeViewModel;
import space.app.ViewModel.UserViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class ContributeCafeInformationActivity extends AppCompatActivity implements CafeImageRecyclerViewAdapter.OnImageRemovedListener, MenuImageRecyclerViewAdapter.OnImageRemovedListener {
    private ImageView BackPerson;
    private RecyclerView recyclerViewCafe,recyclerViewMenu;
    private FloatingActionButton FloatingActionButtonCafe, FloatingActionButtonMenu;
    private ArrayList<Uri> ChooseImageListCafe, ChooseImageListMenu;
    private static final int MIN_IMAGE_COUNT = 3;
    private static final int PERMISSION_REQUEST_CODE = 2, PICK_IMAGE_REQUEST_CODE = 1;
    private EditText edtMap, edtContact, edtDescription, edtTime, edtPrice, edtNameCafe, edtAddress;
    private MaterialButton btnSendContributeCafe;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private AlertDialog alertDialog;
    private UserEntity user = new UserEntity();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute_cafe_information);

        // Back to FragmentMe
        BackPerson = findViewById(R.id.BackPerson);
        BackPerson.setOnClickListener(v -> {
            finish();
        });

        firebaseAuth = FirebaseAuth.getInstance();
        String email = firebaseAuth.getCurrentUser().getEmail();
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
        FloatingActionButtonCafe.setOnClickListener(v -> pickImageGalleryForCafe(PICK_IMAGE_REQUEST_CODE));
        FloatingActionButtonMenu.setOnClickListener(v -> pickImageGalleryForMenu(PICK_IMAGE_REQUEST_CODE + 1));

        UserViewModel userViewModel = new ViewModelProvider(ContributeCafeInformationActivity.this).get(UserViewModel.class);
        userViewModel.getUserByEmail(email).observe(ContributeCafeInformationActivity.this, new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                if (userEntity != null) {
                    user = userEntity;
                }
            }
        });


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

            String resName = edtNameCafe.getText().toString();
            String address = edtAddress.getText().toString();
            String purpose = ((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString();
            String price = edtPrice.getText().toString();
            String contact = edtContact.getText().toString();

            if (resName.isEmpty() || address.isEmpty() || purpose.isEmpty() || price.isEmpty()) {
                Toast.makeText(this, "Hãy nhập Tên quán, Địa chỉ và Giá tiền bạn nhé 😊 !", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!contact.isEmpty() && !isValidContact(contact)) {
                Toast.makeText(this, "Số điện thoại không hợp lệ 😢", Toast.LENGTH_SHORT).show();
                return;
            }
            if (ChooseImageListCafe.size() < MIN_IMAGE_COUNT || ChooseImageListMenu.size() < MIN_IMAGE_COUNT) {
                Toast.makeText(this, "Bạn chưa tải ảnh lên rồi ! 😢", Toast.LENGTH_SHORT).show();
                return;
            }
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.loading_compo,null);
            TextView textView = view.findViewById(R.id.nameOfLoading);
            textView.setText("Đang thêm quán! Vui lòng chờ ...");
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ContributeCafeInformationActivity.this);
            builder.setView(view);
            builder.setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();
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

        CafeViewModel cafeViewModel = new ViewModelProvider(ContributeCafeInformationActivity.this).get(CafeViewModel.class);
        cafeViewModel.getCafeByResName(resName).observe(ContributeCafeInformationActivity.this, new Observer<List<CafeEntity>>() {
            @Override
            public void onChanged(List<CafeEntity> cafeEntities) {
                if(cafeEntities.isEmpty()){
                    Log.d("CafeUpload", "Bắt đầu lưu ảnh và thông tin quán");

                    FirebaseDatabase firebase = FirebaseDatabase.getInstance();
                    String idCafe = Utils.hashEmail(resName);
                    DatabaseReference cafeRef = firebase.getReference("Cafe").child(idCafe);

                    Log.d("CafeUpload", " cafeId: " + idCafe);

                    Cafe cafe = new Cafe();

                    MutableLiveData<Integer> count = new MutableLiveData<>();
                    count.setValue(0);
                    uploadImages(ChooseImageListCafe, "CafeImages",idCafe, new UploadImagesCallback() {
                        @Override
                        public void onImagesUploaded(String imageUrl) {

                            cafe.setImages(imageUrl);
                            count.postValue(count.getValue() + 1);
                        }
                    });

                    uploadImages(ChooseImageListMenu, "MenuImages",idCafe, new UploadImagesCallback() {
                        @Override
                        public void onImagesUploaded(String imageUrl) {
                            // Log menu image URLs
                            cafe.setMenu(imageUrl);
                            cafe.setResName(resName);
                            cafe.setLink(link);
                            cafe.setAddress(address);
                            cafe.setDescribe(describe);
                            cafe.setPrice(Float.valueOf(price));
                            cafe.setTimeOpen(timeOpen);
                            cafe.setContact(contact);
                            cafe.setPurpose(purpose);
                            cafe.setIdCafe(idCafe);
                            cafe.setIdUser(user.getIdUser());
                            Log.d("CafeUpload", " Cafe : " + cafe.toString());
                            count.postValue(count.getValue() + 1);
                        }
                    });

                    count.observe(ContributeCafeInformationActivity.this, new Observer<Integer>() {
                        @Override
                        public void onChanged(@Nullable Integer value) {
                            if (value != null && value == 2) {
                                alertDialog.dismiss();
                                cafeRef.setValue(cafe)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(ContributeCafeInformationActivity.this, "Thông tin quán đã được lưu thành công!", Toast.LENGTH_SHORT).show();
                                            updateCafeCount();
                                            count.removeObservers(ContributeCafeInformationActivity.this);
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("CafeUpload", "Lỗi khi thêm quán: " + e.getMessage());
                                            Toast.makeText(ContributeCafeInformationActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(ContributeCafeInformationActivity.this, "Quán đã tồn tại!", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },3000);
                }
            }
        });
    }

    private boolean isValidContact(String contact) {
        return contact.matches("^0\\d{9}$");
    }

    // update số lượng quán đã thêm
    private void updateCafeCount() {
        int currentCount = getCurrentCafeCount();
        currentCount++;
        saveCafeCount(currentCount);

    }

    private int getCurrentCafeCount() {
        SharedPreferences sharedPreferences = getSharedPreferences("CafeCountPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("cafeCount", 0); // Trả về số lượng quán, mặc định là 0
    }

    private void saveCafeCount(int count) {
        // Lưu số lượng quán mới vào SharedPreferences hoặc cập nhật vào Database
        // Ví dụ: lưu vào SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("CafeCountPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("cafeCount", count);
        editor.apply();
    }

    // up ảnh và thông tin quán
    private void uploadImages(ArrayList<Uri> imageUris, String folderName,String idRes, UploadImagesCallback callback) {
        ArrayList<String> imageUrls = new ArrayList<>();

        for (Uri imageUri : imageUris) {
            String uniqueID = UUID.randomUUID().toString();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Img/restaurents/"+idRes+"/"+folderName+"/"+uniqueID);
            UploadTask uploadTask = storageRef.putFile(imageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                        // Get URL of the uploaded image
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Add URL to the list
                            imageUrls.add(uri.toString());

                            // Check if all images have been uploaded
                            if (imageUrls.size() == imageUris.size()) {
                                // Call callback to return the list of image URLs
                                callback.onImagesUploaded("Img/restaurents/"+idRes);
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ContributeCafeInformationActivity.this, "Lỗi khi tải ảnh lên: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    interface UploadImagesCallback {
        void onImagesUploaded(String imageUrl);
    }

    // kiểm tra quyền truy cập bộ nhớ ảnh
//    private void checkPermissionAndPickImageForCafe(int pickImageRequestCode) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(ContributeCafeInformationActivity.this,
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(ContributeCafeInformationActivity.this,
//                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//            } else {
//                pickImageGalleryForCafe(pickImageRequestCode);
//            }
//        } else {
//            pickImageGalleryForCafe(pickImageRequestCode);
//        }
//    }

    private void pickImageGalleryForCafe(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCode);
    }


//    private void checkPermissionAndPickImageForMenu(int i) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(ContributeCafeInformationActivity.this,
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(ContributeCafeInformationActivity.this,
//                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//            } else {
//                pickImageGalleryForMenu(i);
//            }
//        } else {
//            pickImageGalleryForMenu(i);
//        }
//    }

    private void pickImageGalleryForMenu(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE + 1);
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

//            if (selectedImages.size() < MIN_IMAGE_COUNT) {
//                Toast.makeText(this, "Bạn cần up tối thiểu 3 ảnh", Toast.LENGTH_SHORT).show();
//            } else {
//                if (requestCode == PICK_IMAGE_REQUEST_CODE) {
//                    ChooseImageListCafe.clear();
//                    ChooseImageListCafe.addAll(selectedImages);
//                    setAdapter(ChooseImageListCafe, requestCode);
//                } else if (requestCode == PICK_IMAGE_REQUEST_CODE + 1) {
//                    ChooseImageListMenu.clear();
//                    ChooseImageListMenu.addAll(selectedImages);
//                    setAdapter(ChooseImageListMenu, requestCode);
//                }
//            }
            runOnUiThread(() -> {
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
            });
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

//        @Override
//        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//            if (requestCode == PERMISSION_REQUEST_CODE) {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//    //                pickImageGalleryForCafe(requestCode);
//    //                pickImageGalleryForMenu(requestCode);
//                } else {
//                    Toast.makeText(this, "Permission denied. Cannot pick images.", Toast.LENGTH_SHORT).show();
//    //                new Handler(Looper.getMainLooper()).postDelayed(() -> {
//    //                    ActivityCompat.requestPermissions(ContributeCafeInformationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//    //                }, 2000);
//                }
//            }
//        }


    @Override
    public void onImageRemoved() {
        // Handle image removal, if needed
    }

}