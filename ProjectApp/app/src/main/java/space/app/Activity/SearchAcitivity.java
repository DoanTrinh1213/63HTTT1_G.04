package space.app.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import space.app.Adapter.CafeAdapter;
import space.app.Adapter.SearchGuessAdapter;
import space.app.Adapter.SearchResultAdapter;
import space.app.Database.Entity.SearchResultEntity;
import space.app.Database.SQLiteDatabaseHelper;
import space.app.Interface.RecyclerViewOnClickItem;
import space.app.Interface.RecyclerViewSearchOnClickItem;
import space.app.Model.Cafe;
import space.app.R;
import space.app.ViewModel.SearchViewModel;

public class SearchAcitivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_acitivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.leftMargin = insets.left;
            mlp.bottomMargin = insets.bottom;
            mlp.rightMargin = insets.right;
            v.setLayoutParams(mlp);
            return WindowInsetsCompat.CONSUMED;
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
                Intent intent = new Intent(SearchAcitivity.this, DetailAcitvity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Object_Cafe", cafe);
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
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    String textSearch = searchText.getText().toString().trim();
                    // Create an intent to send back the search results
                    if(!textSearch.isEmpty()){
                        saveSearchResult(textSearch);
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isRefresh",false);
                        editor.apply();
                    }
                    else{
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
                        sharedPreferences.getBoolean("isRefresh",true);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isRefresh",true);
                        editor.apply();
                    }
                    Intent intent = new Intent(SearchAcitivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });

        ImageView clearSearch = findViewById(R.id.closeSearch);
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText("");
            }
        });

        SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        // Quan sát dữ liệu LiveData từ ViewModel
        searchViewModel.getAllSearchResults().observe(this, new Observer<List<SearchResultEntity>>() {
            @Override
            public void onChanged(List<SearchResultEntity> searchResultEntities) {
                RecyclerView rclViewSearch = findViewById(R.id.recycleViewSearchResult);
                rclViewSearch.setLayoutManager(new LinearLayoutManager(SearchAcitivity.this));
                ArrayList<SearchResultEntity> resultEntities = new ArrayList<>();
                HashSet<String> existingTexts = new HashSet<>();
                for (SearchResultEntity rs : searchResultEntities) {
                    if (!existingTexts.contains(rs.getSearchQuery())) {
                        resultEntities.add(rs);
                        existingTexts.add(rs.getSearchQuery());
                    }
                }
                rclViewSearch.setAdapter(new SearchResultAdapter(resultEntities, new RecyclerViewSearchOnClickItem() {
                    @Override
                    public void onItemClickSearch(SearchResultEntity searchResult) {
                        saveSearchResult(searchResult.getSearchQuery());
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isRefresh",false);
                        editor.apply();
                        Intent intent = new Intent(SearchAcitivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }));
            }
        });
    }

    private void saveSearchResult(String textSearch) {
        SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        SearchResultEntity searchResult = new SearchResultEntity();
        searchResult.setSearchQuery(textSearch);
        searchViewModel.insertSearchResult(searchResult);
    }
}