package space.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import space.app.R;
import space.app.UI.Fragment.FragmentMe;

public class EditInformationActivity extends AppCompatActivity {
    private ImageView iconBackPerson ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_information);

        iconBackPerson = findViewById(R.id.iconBackPerson);
        iconBackPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                iconBackPerson.setVisibility(View.GONE);
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container_Me,new FragmentMe()).addToBackStack(null).commit();

            }
        });
    }
}