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
        private EditText edtMap;
        private EditText edtContact;
        private EditText edtDescription;
        private EditText edtTime;
        private EditText edtPrice;
        private EditText edtNameCafe;
        private EditText edtAddress;
        private MaterialButton btnSendContributeCafe;

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