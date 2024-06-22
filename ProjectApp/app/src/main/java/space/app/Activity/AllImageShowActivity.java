package space.app.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import space.app.Adapter.ImageAdapter;
import space.app.Helper.Dialog;
import space.app.Interface.HeightWidthOnSet;
import space.app.Interface.RecyclerViewOnClickItem;
import space.app.R;

public class AllImageShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_image_show);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        String txtName = intent.getStringExtra("nameCafe");
        List<Uri> uri = (List<Uri>) intent.getSerializableExtra("ImageList");
        RecyclerView recyclerView = findViewById(R.id.allImage);
        recyclerView.setAdapter(new ImageAdapter(AllImageShowActivity.this, uri, new RecyclerViewOnClickItem() {
            @Override
            public void onItemClickImage(Uri uri) {
                Dialog.openDialogImage(AllImageShowActivity.this, uri);
            }
        }, new HeightWidthOnSet() {
            @Override
            public void setParams(View view) {
                view.getLayoutParams().width= 600;
                view.getLayoutParams().height = 400;
            }
        }));
        recyclerView.setLayoutManager(new GridLayoutManager(AllImageShowActivity.this, 2));

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView name = findViewById(R.id.name);
        name.setText(txtName);
    }
}