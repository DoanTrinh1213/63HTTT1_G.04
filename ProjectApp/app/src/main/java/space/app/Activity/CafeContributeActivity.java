package space.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import space.app.Adapter.CafeAdapter;
import space.app.Interface.RecyclerViewOnClickItem;
import space.app.Model.Cafe;
import space.app.R;
import space.app.UI.Fragment.FragmentShop;

public class CafeContributeActivity extends AppCompatActivity {
    private TextView etdCount;
    private ImageView iconBackPersonn;
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String CAFE_COUNT_PREFS = "CafeCountPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_contribute);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        etdCount = findViewById(R.id.etdCount);
        iconBackPersonn = findViewById(R.id.iconBackPersonn);
        iconBackPersonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.ListContributeCafe);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateCafeCountDisplay();
    }

    private void updateCafeCountDisplay() {
        int currentCount = getCurrentCafeCount();
        etdCount.setText(String.valueOf(currentCount));

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            saveCafeCountToFirebase(userId, currentCount);
        } else {
            Toast.makeText(this, "Không thể lấy được thông tin người dùng", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCafeCountToFirebase(String userId, int cafeCount) {
        mDatabase.child("CountCafeContribute").child(userId).setValue(cafeCount)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(CafeContributeActivity.this, "Lưu không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }





    private int getCurrentCafeCount() {
        SharedPreferences sharedPreferences = getSharedPreferences(CAFE_COUNT_PREFS, MODE_PRIVATE);
        return sharedPreferences.getInt("cafeCount", 0);
    }
}