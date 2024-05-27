package space.app.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;

import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import space.app.Database.CafeDatabase;
import space.app.Model.Cafe;
import space.app.R;
import space.app.UI.Fragment.FragmentAuth;
import space.app.UI.Fragment.FragmentBookmark;
import space.app.UI.Fragment.FragmentCafeHomePage;
import space.app.UI.Fragment.FragmentLogin;
import space.app.UI.Fragment.FragmentMe;
import space.app.ViewModel.CafeViewModel;

public class MainActivity extends AppCompatActivity {

    private CafeViewModel cafeViewModel;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

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
        List<Cafe> cafeList = (List<Cafe>) getIntent().getSerializableExtra("cafeList");
        if (cafeList != null) {
            cafeViewModel.setCafeList(cafeList);
        }
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference("Cafe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Cafe> cafeList = new ArrayList<>();
                executorService.execute(()->{
                    CafeDatabase.getInstance(MainActivity.this).cafeDAO().deleteAll();
                });
                for (DataSnapshot data : snapshot.getChildren()) {
                    Cafe cafe = data.getValue(Cafe.class);
                    if (cafe != null) {
                        cafeList.add(cafe);
                    }
                }
                if(cafeList!=null){
                    cafeViewModel.setCafeList(cafeList);
                }
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
            replaceFragment(new FragmentCafeHomePage(), false);
        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenu);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                 if (menuItem.getItemId() == R.id.home) {
                    if(isLoggedIn==false){
                        replaceFragment(new FragmentAuth(), false);
                    }
                    else{
                        replaceFragment(new FragmentCafeHomePage(), false);
                    }
                    return true;
                } else if (menuItem.getItemId() == R.id.bookmark) {
                    if(isLoggedIn==false){
                        replaceFragment(new FragmentBookmark(), false);
                    }
                    return true;
                } else {
                    if(isLoggedIn==false)
                    {
                        replaceFragment(new FragmentLogin(), false);
                    }
                    else
                        replaceFragment(new FragmentMe(), false);
                    return true;
                }
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = getSupportFragmentManager();
               if(bottomNavigationView.getSelectedItemId()==R.id.home){
                        Log.d("Tag","OnBackPressedCallback : "+fragmentManager.getBackStackEntryCount());
                        moveTaskToBack(true);
                }
                else{
                    bottomNavigationView.setSelectedItemId(R.id.home);
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Main","OnStart call");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Main","OnResume call");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Main","OnRestart call");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Main","onPause call");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Main","onStop call");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Main","onDestroy call");

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
}