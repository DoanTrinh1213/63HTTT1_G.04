package space.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcherOwner;
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

import space.app.Adapter.SearchGuessAdapter;
import space.app.Adapter.SearchResultAdapter;
import space.app.Database.Entity.CafeEntity;
import space.app.Database.Entity.SearchResultEntity;
import space.app.Helper.Converter;
import space.app.Interface.RecyclerViewOnClickItem;
import space.app.Interface.RecyclerViewSearchOnClickItem;
import space.app.Model.Cafe;
import space.app.R;
import space.app.ViewModel.CafeViewModel;
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
        Bundle bundle= new Bundle();
        bundle = getIntent().getBundleExtra("BundleData");

        List<Cafe> cafes = new ArrayList<>();
        CafeViewModel cafeViewModel = new ViewModelProvider(this).get(CafeViewModel.class);
        cafeViewModel.getAllCafe().observe(this, new Observer<List<CafeEntity>>() {
            @Override
            public void onChanged(List<CafeEntity> cafeEntities) {
                for (CafeEntity cafeEntity : cafeEntities) {
                    Cafe cafe = new Cafe();
                    cafe = Converter.convertCafeEntityToCafeModel(cafeEntity);
                    cafes.add(cafe);
                }
                RecyclerView recyclerView = findViewById(R.id.recycleViewSearch);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchAcitivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(new SearchGuessAdapter(new ArrayList<>(cafes), new RecyclerViewOnClickItem() {
                    @Override
                    public void onItemClickCafe(Cafe cafe) {
                        Intent intent = new Intent(SearchAcitivity.this, DetailAcitvity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Object_Cafe", cafe);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }));
            }
        });


        EditText searchText = findViewById(R.id.textSearch);

        searchText.setText(bundle.getString("textSearch"));

        ImageView imageBack = findViewById(R.id.backSearch);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    String textSearch = searchText.getText().toString().trim();
                    // Create an intent to send back the search results
                    if (!textSearch.isEmpty()) {
                        saveSearchResult(textSearch);
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        sharedPreferences.getBoolean("isRefresh", true);
                        sharedPreferences.getBoolean("isSearch", true);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isRefresh", false);
                        editor.putBoolean("isSearch", true);
                        editor.apply();
                    } else {
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        sharedPreferences.getBoolean("isRefresh", true);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isRefresh", true);
                        editor.putBoolean("isSearch", false);
                        editor.apply();
                    }
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    Boolean isRefresh = sharedPreferences.getBoolean("isRefresh", true);
                    Log.d("isRefresh", isRefresh.toString());
                    Intent intent = new Intent(SearchAcitivity.this, MainActivity.class);
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
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isRefresh", false);
                        editor.putBoolean("isSearch", true);
                        editor.apply();
                        Intent intent = new Intent(SearchAcitivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }));
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(searchText.getText().toString().isEmpty()){
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isSearch",false);
                    editor.putBoolean("isRefresh",true);
                    editor.apply();
                    Intent intent =new Intent(SearchAcitivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this,callback);
    }

    private void saveSearchResult(String textSearch) {
        SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        SearchResultEntity searchResult = new SearchResultEntity();
        searchResult.setSearchQuery(textSearch);
        searchViewModel.insertSearchResult(searchResult);
    }
}