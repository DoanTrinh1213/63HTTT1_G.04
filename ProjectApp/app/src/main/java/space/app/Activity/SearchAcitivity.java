package space.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import space.app.Adapter.CafeAdapter;
import space.app.Adapter.SearchGuessAdapter;
import space.app.Interface.RecyclerViewOnClickItem;
import space.app.Model.Cafe;
import space.app.R;

public class SearchAcitivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_acitivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ArrayList<Cafe> cafes = new ArrayList<Cafe>();
        Cafe cafe = new Cafe("123", "CafePro1", "HaNoi", "Helloworld", (float) 20.3, "Menu", "13:00-24:00", "Hello", "URL", "5", "URL", "Sell");
        cafes.add(cafe);
        Cafe cafe2 = new Cafe("1234", "CafePro12", "HaNoi", "Helloworld", (float) 20.3, "Menu", "13:00-24:00", "Hello", "URL", "5", "URL", "Sell");
        cafes.add(cafe2);
        Cafe cafe3 = new Cafe("1235", "CafePro13", "HaNoi", "Helloworld", (float) 20.3, "Menu", "13:00-24:00", "Hello", "URL", "5", "URL", "Sell");
        cafes.add(cafe3);
        Cafe cafe4 = new Cafe("1236", "CafePro14", "HaNoi", "Helloworld", (float) 20.3, "Menu", "13:00-24:00", "Hello", "URL", "5", "URL", "Sell");
        cafes.add(cafe4);

        RecyclerView recyclerView = findViewById(R.id.recycleViewSearch);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new SearchGuessAdapter(cafes, new RecyclerViewOnClickItem() {
            @Override
            public void onItemClickCafe(Cafe cafe) {
                Intent intent = new Intent(SearchAcitivity.this,DetailAcitvity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Object_Cafe",cafe);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }));

        ImageView imageBack = findViewById(R.id.backSearch);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        EditText searchText = findViewById(R.id.textSearch);

        ImageView clearSearch = findViewById(R.id.closeSearch);
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText("");
            }
        });
    }
}