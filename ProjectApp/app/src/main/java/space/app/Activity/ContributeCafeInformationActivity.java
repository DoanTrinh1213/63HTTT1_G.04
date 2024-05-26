package space.app.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import space.app.Adapter.RecyclerAapter;
import space.app.Adapter.CustomSpinnerAdapter;
import space.app.R;
import space.app.UI.Fragment.FragmentMe;

public class ContributeCafeInformationActivity extends AppCompatActivity {
    private ImageView BackPerson;
    private static final int Read_Permission = 101;
    private static final int PICK_IMAGE = 1;
    private ActivityResultLauncher<Intent> mGetContent;


    RecyclerView recyclerView;
    TextView textView;
    FloatingActionButton floatingActionButtonCafe;
    ArrayList<Uri> uri = new ArrayList<>();
    RecyclerAapter adapter;


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

        // update Image
        mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri imageUri = data.getData();
                        if (imageUri != null) {
                            uri.add(imageUri);
                            adapter.notifyDataSetChanged();
                            textView.setText("Ảnh(" + uri.size() + ")");
                        }
                    }
                } else {
                    Toast.makeText(ContributeCafeInformationActivity.this, "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RecyclerAapter adapterRecycler;

        recyclerView = findViewById(R.id.recyclerView_Gallery_Images_Cafe);
        floatingActionButtonCafe = findViewById(R.id.floatingActionButtonCafe);
        textView = findViewById(R.id.textView); // Đảm bảo textView đã được khởi tạo
        adapterRecycler = new RecyclerAapter(uri);
        recyclerView.setLayoutManager(new LinearLayoutManager(ContributeCafeInformationActivity.this));
        recyclerView.setAdapter(adapterRecycler);

        floatingActionButtonCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ContributeCafeInformationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestStoragePermission();
                } else {
                    openImagePicker();
                }
            }
        });

    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Yêu cầu quyền truy cập")
                    .setMessage("Ứng dụng cần quyền truy cập để hiển thị ảnh từ bộ nhớ.")
                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ContributeCafeInformationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);
                        }
                    })
                    .setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mGetContent.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Read_Permission) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();

            } else {
                Toast.makeText(this, "Quyền truy cập bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
