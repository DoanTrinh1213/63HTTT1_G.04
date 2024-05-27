package space.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import space.app.Database.CafeDatabase;
import space.app.Database.SearchDatabase;
import space.app.Model.Cafe;
import space.app.R;
import space.app.ViewModel.CafeViewModel;
import space.app.ViewModel.SearchViewModel;

public class SplashScreen extends AppCompatActivity {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<Cafe> cafeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        executorService.execute(() -> {
            SearchDatabase.getInstance(this).searchResultDao().deleteAll();
        });
        executorService.execute(()->{
            cafeList = new ArrayList<>();
            CafeDatabase.getInstance(this).cafeDAO().deleteAll();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.getReference("Cafe").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    cafeList.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Cafe cafe = data.getValue(Cafe.class);
                        if (cafe != null) {
                            cafeList.add(cafe);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SplashScreen.this, "Sorry , connect to firebase is failed", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                intent.putExtra("cafeList", new ArrayList<>(cafeList));
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}