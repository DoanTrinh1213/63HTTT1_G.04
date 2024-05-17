package space.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;

import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import space.app.UI.Fragment.FragmentAuth;
import space.app.UI.Fragment.FragmentBookmark;
import space.app.UI.Fragment.FragmentContact;
import space.app.UI.Fragment.FragmentLogin;
import space.app.UI.Fragment.FragmentMe;
import space.app.UI.Fragment.FragmentRegister;

public class MainActivity extends AppCompatActivity {

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
            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", true); // Lưu trạng thái đăng nhập là true
            editor.apply();
        } else {
            replaceFragment(new FragmentContact(), false);
            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false); // Lưu trạng thái đăng nhập là false khi đăng xuất
            editor.apply();
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
                        replaceFragment(new FragmentContact(), false);
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


    public void openLoginActivity() {
        replaceFragment(new FragmentLogin(), true);
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
}