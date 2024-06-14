package space.app.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
        import android.content.SharedPreferences;
        import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.graphics.Insets;
        import androidx.core.view.ViewCompat;
        import androidx.core.view.WindowInsetsCompat;

        import space.app.R;

public class CafeContributeActivity extends AppCompatActivity {
    private EditText etdCount;
    private ImageView iconBackPersonn;
    private static final String CAFE_COUNT_PREFS = "CafeCountPrefs";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cafe_contribute);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etdCount = findViewById(R.id.etdCount);
        iconBackPersonn = findViewById(R.id.iconBackPersonn);
        iconBackPersonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Cập nhật số lượng quán ban đầu
        updateCafeCountDisplay();
    }

    private void updateCafeCountDisplay() {
        // Lấy số lượng quán từ SharedPreferences và hiển thị lên etdCount
        int currentCount = getCurrentCafeCount();
        etdCount.setText(String.valueOf(currentCount));
    }

    private int getCurrentCafeCount() {
        // Mặc định là 0 nếu không có giá trị
        SharedPreferences sharedPreferences = getSharedPreferences(CAFE_COUNT_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("cafeCount", 0);
    }
}
