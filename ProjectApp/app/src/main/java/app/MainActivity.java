package add;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import space.app.R;
import add.User;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
           DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Cafe");
            Cafe cafe1 = new Cafe("Nana", "Khâm Thiên", "Bánh", 20F, "", "",
                    "", "", "", "");
//            firebaseDatabase.setValue("Hello,world!");
            firebaseDatabase.setValue(cafe1);
            return insets;


        });
    }
}