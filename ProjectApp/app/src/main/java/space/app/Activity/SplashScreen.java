package space.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import space.app.Database.DatabaseRoom;
import space.app.Helper.LocationUtils;
import space.app.Model.Cafe;
import space.app.R;

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


//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                String url = "https://maps.app.goo.gl/2USx1NPhGjaunFbV7";
//                try {
//                    url = LocationUtils.expandUrl(url);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                LocationUtils.LatLng laslong = LocationUtils.extractLatLngFromUrl(url);
//                Log.d("Location",String.valueOf(laslong.getLatitude()) +","+ String.valueOf(laslong.getLongitude()));
//            }
//        });

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Không có kết nối mạng. Vui lòng kiểm tra kết nối và thử lại!", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 5000);
            return;
        }
        executorService.execute(() -> {
            DatabaseRoom.getInstance(this).searchResultDao().deleteAll();
            DatabaseRoom.getInstance(this).cafeDAO().deleteAll();
        });
//        executorService.execute(() -> {
//            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//            firebaseDatabase.getReference("Cafe").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    cafeList=new ArrayList<>();
//                    cafeList.clear();
//                    for (DataSnapshot data : snapshot.getChildren()) {
//                        Cafe cafe = data.getValue(Cafe.class);
//                        if (cafe != null) {
//                            cafeList.add(cafe);
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(SplashScreen.this, "Sorry , connect to firebase is failed", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            });
//        });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
//                intent.putExtra("cafeList", new ArrayList<>(cafeList));
//                startActivity(intent);
//                finish();
//            }
//        }, 2000);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference connectedRef = database.getReference("Cafe");

        connectedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // If connected, proceed with navigation after delay
                Log.d("Connected", "Connect thanh cong");
                cafeList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Cafe cafe = data.getValue(Cafe.class);
                    if (cafe != null) {
                        cafeList.add(cafe);
                    }
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("isRefresh");
                        editor.remove("isSearch");
                        editor.apply();
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        intent.putExtra("cafeList", new ArrayList<>(cafeList));
                        startActivity(intent);
                        finish();
                    }
                }, 2000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SplashScreen.this, "Firebase connection check failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SplashScreen.this, "Không thể kết nối tới máy chủ , vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, 2000); // Optionally delay before closing the app to let the user read the message.
            }
        });

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}