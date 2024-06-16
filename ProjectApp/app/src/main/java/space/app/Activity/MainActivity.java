package space.app.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import space.app.DAO.CafeDAO;
import space.app.Database.Entity.CafeEntity;
import space.app.Database.Entity.UserEntity;
import space.app.Database.RoomDatabase;
import space.app.Helper.Utils;
import space.app.Model.Cafe;
import space.app.R;
import space.app.UI.Fragment.FragmentAuth;
import space.app.UI.Fragment.FragmentBookmark;
import space.app.UI.Fragment.FragmentCafeHomePage;
import space.app.UI.Fragment.FragmentLogin;
import space.app.UI.Fragment.FragmentMe;
import space.app.ViewModel.CafeViewModel;
import space.app.ViewModel.UserViewModel;

public class MainActivity extends AppCompatActivity {

    private CafeViewModel cafeViewModel;
    private UserViewModel userViewModel;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private RoomDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.leftMargin = insets.left;
            mlp.bottomMargin = insets.bottom;
            mlp.rightMargin = insets.right;
            v.setLayoutParams(mlp);
            return WindowInsetsCompat.CONSUMED;
        });
        cafeViewModel = new ViewModelProvider(this).get(CafeViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        List<Cafe> cafeList = (List<Cafe>) getIntent().getSerializableExtra("cafeList");
        if (cafeList != null) {
            executorService.execute(() -> {
                RoomDatabase.getInstance(MainActivity.this).cafeDAO().deleteAll();
                // CafeRepository cafeRepo = new CafeRepository(getApplication());
                // cafeRepo.deleteAll();
            });
            Log.d("Cafe", "Insert lần đầu tạo main");
            cafeViewModel.setCafeList(cafeList);
        }
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference("Cafe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Cafe> cafeList = new ArrayList<>();
                executorService.execute(() -> {
                    Log.d("Cafe", "Insert sau khi có sự thay đổi");
                    RoomDatabase.getInstance(MainActivity.this).cafeDAO().deleteAll();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Cafe cafe = data.getValue(Cafe.class);
                        if (cafe != null) {
                            cafeList.add(cafe);
                        }
                    }
                    if (cafeList != null) {
                        cafeViewModel.setCafeList(cafeList);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                finish();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("isLoggedIn", false); // Lưu trạng thái đăng nhập là true
//        editor.apply();

        // Kiểm tra trạng thái đăng nhập
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false); // Mặc định là false nếu không có giá trị được lưu trữ
        Log.d("Message", String.valueOf(isLoggedIn));
        if (isLoggedIn == false) {
            replaceFragment(new FragmentAuth(), false);
        } else {
            setUser();
            replaceFragment(new FragmentCafeHomePage(), false);
        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenu);
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            final MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
            View view = bottomNavigationView.findViewById(menuItem.getItemId());
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true; // Trả về true để chỉ định rằng sự kiện đã được xử lý
                }
            });
        }
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.home) {
                    if (isLoggedIn == false) {
                        replaceFragment(new FragmentAuth(), false);
                    } else {
                        replaceFragment(new FragmentCafeHomePage(), false);
                    }
                    return true;
                } else if (menuItem.getItemId() == R.id.bookmark) {
                    if (isLoggedIn == false) {
                        replaceFragment(new FragmentLogin(), false);
                    } else {
                        replaceFragment(new FragmentBookmark(), false);
                    }
                    return true;
                } else {
                    if (isLoggedIn == false) {
                        replaceFragment(new FragmentLogin(), false);
                    } else
                        replaceFragment(new FragmentMe(), false);
                    return true;
                }
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (bottomNavigationView.getSelectedItemId() == R.id.home) {
                    Log.d("Tag", "OnBackPressedCallback : " + fragmentManager.getBackStackEntryCount());
                    moveTaskToBack(true);
                } else {
                    bottomNavigationView.setSelectedItemId(R.id.home);
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Main", "OnStart call");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Main", "OnResume call");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Main", "OnRestart call");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Main", "onPause call");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Main", "onStop call");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Main", "onDestroy call");
    }

    public void replaceFragment(Fragment fragment, boolean backStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        if (backStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }


    //    public void openFragmentEditInformation() {
//        replaceFragment(new FragmentEditInformation(), true);
//    }
    public void setSelectedBottomNavItem(int itemId) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenu);
        bottomNavigationView.setSelectedItemId(itemId);
    }

    public void setUser() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String idUser = Utils.hashEmail(mAuth.getCurrentUser().getEmail());
            firebaseDatabase.getReference("User").child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserEntity user = new UserEntity();
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    sharedPreferences.getString("id", "id");
                    sharedPreferences.getString("imageUrl", "imageUrl");
                    sharedPreferences.getString("description", "description");
                    sharedPreferences.getString("userName", "userName");

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    String name = snapshot.child("userName").getValue(String.class);
                    String id = snapshot.child("id").getValue(String.class);
                    String imageUrl = snapshot.child("image").getValue(String.class);
                    String description = snapshot.child("describe").getValue(String.class);

                    user.setIdUser(id);
                    user.setUsername(name);
                    user.setDescribe(description);
                    Log.d("print id 1",user.getUsername());
                    if (id == null) {
                        Log.d("id", "NULL");
                    }
                    id = Utils.hash32b(id);
                    editor.putString("id", id);
                    editor.putString("userName", name);
                    editor.putString("description", description);

                    if (imageUrl!=null) {
                        Log.d("Image url of user", imageUrl.toString());
                        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
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
                        // cái image nó chưa lưu xong mà cái này đã được chạy rồi nên bị lỗi :v
                        storageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Uri fileUri = Uri.fromFile(finalFile);
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("imageUrl", fileUri.toString());
                                user.setImageUrl(fileUri.toString());
                                editor.apply();
                                Log.d("Firebase", "Download image");
                                executorService.execute(() -> {
                                    Log.d("print id 2",user.getUsername());
                                    Log.d("Url", user.getImageUrl());
                                    RoomDatabase.getInstance(MainActivity.this).userDAO().DeleteAllUser();
                                    RoomDatabase.getInstance(MainActivity.this).userDAO().InsertUser(user);
                                });
                                executorService.shutdown();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Xử lý trường hợp tải ảnh không thành công
                                Log.e("Firebase", "Error downloading image: " + e.getMessage());
                            }
                        });

                    } else {
                        Toast.makeText(MainActivity.this, "Không thể tải ảnh , vui lòng thêm lại ảnh hoặc kiểm tra kết nối mạng và thử lại!", Toast.LENGTH_SHORT);
                    }
                    editor.apply();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
}