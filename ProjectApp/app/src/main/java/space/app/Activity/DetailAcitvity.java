package space.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import space.app.Model.Cafe;
import space.app.R;
import space.app.UI.Fragment.FragmentAuth;
import space.app.UI.Fragment.FragmentBookmark;
import space.app.UI.Fragment.FragmentCafeHomePage;
import space.app.UI.Fragment.FragmentLogin;
import space.app.UI.Fragment.FragmentMe;
import space.app.UI.Fragment.FragmentShop;

public class DetailAcitvity extends AppCompatActivity {

    FragmentShop fragmentShop;
    FragmentBookmark fragmentBookmark;
    FragmentMe fragmentMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_acitvity);
        replaceFragment(new FragmentShop(),false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fragmentShop= new FragmentShop();
        fragmentMe=new FragmentMe();
        fragmentBookmark = new FragmentBookmark();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fragmentShop.setArguments(extras);
            replaceFragment(fragmentShop,false);
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.leftMargin = insets.left;
            mlp.bottomMargin = insets.bottom;
            mlp.rightMargin = insets.right;
            v.setLayoutParams(mlp);
            return WindowInsetsCompat.CONSUMED;
        });
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
                    replaceFragment(fragmentShop,false);
                    return true;
                } else if (menuItem.getItemId() == R.id.bookmark) {
                    replaceFragment(fragmentBookmark,false);
                    return true;
                } else {
                    replaceFragment(fragmentMe, false);
                    return true;
                }
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if(bottomNavigationView.getSelectedItemId()!=R.id.home){
                    bottomNavigationView.setSelectedItemId(R.id.home);
                }
                else{
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
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

    @Override
    protected void onResume() {
        super.onResume();
    }
}