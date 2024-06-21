package space.app.UI.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textview.MaterialTextView;
import com.google.android.play.integrity.internal.o;
import com.google.firebase.database.FirebaseDatabase;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import space.app.Activity.DetailAcitvity;
import space.app.Activity.MainActivity;
import space.app.Activity.SearchAcitivity;
import space.app.Adapter.CafeAdapter;
import space.app.Database.Entity.CafeEntity;
import space.app.Database.Entity.SearchResultEntity;
import space.app.Helper.Converter;
import space.app.Interface.RecyclerViewOnClickItem;
import space.app.Model.Cafe;
import space.app.R;
import space.app.ViewModel.CafeViewModel;
import space.app.ViewModel.SearchViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCafeHomePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCafeHomePage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CheckBox checkBox, cBoxAlone, cBoxDate, cBoxDrink, cBoxSelfie, cBoxReadBook, cBoxFriends, cBoxBar, cBoxTime, cBoxWork, cBoxDisWalk, cBoxDisMedium, cBoxDisFar, cBoxPriceCheap, cBoxPriceMedium, cBoxPriceHigh;
    private SeekBar seekBar, seekBarPrice;
    private RadioButton nearlyRadioBtn, cheapestRadioBtn, expensiveRadioBtn;
    private RadioGroup radioGroup;
    private Button resetFilter;
    private SearchViewModel searchViewModel;
    private RecyclerView recyclerViewOther;
    private RecyclerView recyclerView;


    public FragmentCafeHomePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCafeHomePage.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCafeHomePage newInstance(String param1, String param2) {
        FragmentCafeHomePage fragment = new FragmentCafeHomePage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cafe_home_page, container, false);

        List<Cafe> cafes = new ArrayList<Cafe>();

        CafeViewModel cafeViewModel = new ViewModelProvider(this).get(CafeViewModel.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Boolean isSearch = sharedPreferences.getBoolean("isSearch", false);
        Log.d("Cafe", "isSearh : " + isSearch.toString());
        if (isSearch == false) {

            cafeViewModel.getCafesByFindCoffee().observe(getViewLifecycleOwner(), new Observer<List<CafeEntity>>() {
                @Override
                public void onChanged(List<CafeEntity> cafeEntities) {
                    List<Cafe> cafesByFindCoffee = new ArrayList<Cafe>();
                    for (CafeEntity cafeEntity : cafeEntities) {
                        Cafe cafe = new Cafe();
                        cafe = Converter.convertCafeEntityToCafeModel(cafeEntity);
                        cafesByFindCoffee.add(cafe);
                    }
                    updateRecycleViewFindCafe(view, cafesByFindCoffee);
                }
            });
            cafeViewModel.getCafeByTopEvaluate().observe(getViewLifecycleOwner(), new Observer<List<CafeEntity>>() {
                @Override
                public void onChanged(List<CafeEntity> cafeEntities) {
                    cafes.clear();
                    for (CafeEntity cafeEntity : cafeEntities) {
                        Cafe cafe = new Cafe();
                        cafe = Converter.convertCafeEntityToCafeModel(cafeEntity);
                        cafes.add(cafe);
                    }
                    Log.d("Cafe", "Update getCafeByTopEvaluate");
                    updateRecycleViewOrther(view, cafes);
                }
            });
        }


        TextView searchView = view.findViewById(R.id.search_bar_text);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchAcitivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("textSearch", searchView.getText().toString());
                intent.putExtra("BundleData", bundle);
                getActivity().startActivity(intent);
            }
        });
        // Filter
        ImageView iconSetting = view.findViewById(R.id.iconSetting);
        iconSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogFilter(Gravity.BOTTOM);
            }
        });

        // Mục đích
        LinearLayout linearPurpose = view.findViewById(R.id.linearPurpose);
        linearPurpose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogPurpose(Gravity.BOTTOM);
            }
        });
        //Khoảng cách
        LinearLayout linearDistance = view.findViewById(R.id.linearDistance);
        linearDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogDistance(Gravity.BOTTOM);
            }
        });
        // Giá tiền
        LinearLayout linearPrice = view.findViewById(R.id.linearPrice);
        linearPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogPrice(Gravity.BOTTOM);
            }
        });

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        // Quan sát dữ liệu LiveData từ ViewModel
        searchViewModel.getAllSearchResults().observe(getViewLifecycleOwner(), new Observer<List<SearchResultEntity>>() {
            @Override
            public void onChanged(List<SearchResultEntity> searchResults) {
                if (searchResults.size() == 0) {
                    Log.d("Search Result", "Không có gì");
                } else {
                    for (SearchResultEntity searchResult : searchResults) {
                        Log.d("Search Result", searchResult.getSearchQuery());
                    }
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    Boolean isRefresh = sharedPreferences.getBoolean("isRefresh", true);
                    if (!searchResults.isEmpty() && isRefresh == false) {
                        SearchResultEntity lastSearchResult = searchResults.get(searchResults.size() - 1);
                        searchView.setText(lastSearchResult.getSearchQuery());
                        Log.d("Update UI", lastSearchResult.getSearchQuery());
                        List<Cafe> cafesByTerm = new ArrayList<Cafe>();
                        cafeViewModel.getCafesBySearchTerm(lastSearchResult.getSearchQuery()).observe(getViewLifecycleOwner(), new Observer<List<CafeEntity>>() {
                            @Override
                            public void onChanged(List<CafeEntity> cafeEntities) {
                                Log.d("Cafe", "Update getCafesBySearchTerm");
                                if (cafeEntities.size() != 0) {
                                    cafesByTerm.clear();
                                    for (CafeEntity cafeEntity : cafeEntities) {
                                        Cafe cafe = new Cafe();
                                        cafe = Converter.convertCafeEntityToCafeModel(cafeEntity);
                                        cafesByTerm.add(cafe);
                                    }
                                    updateRecycleViewOrther(view, cafesByTerm);
                                }
                            }
                        });
                        cafeViewModel.getCafesBySearchTermAndFindCoffee(lastSearchResult.getSearchQuery()).observe(getViewLifecycleOwner(), new Observer<List<CafeEntity>>() {
                            @Override
                            public void onChanged(List<CafeEntity> cafeEntities) {
                                List<Cafe> cafesByTermFindCoffee = new ArrayList<>();
                                Log.d("Cafe", "Update getCafesBySearchTermAndFindCoffee");
                                if (cafeEntities.size() != 0) {
                                    for (CafeEntity cafeEntity : cafeEntities) {
                                        Cafe cafe = new Cafe();
                                        cafe = Converter.convertCafeEntityToCafeModel(cafeEntity);
                                        cafesByTermFindCoffee.add(cafe);
                                    }
                                    updateRecycleViewFindCafe(view, cafesByTermFindCoffee);
                                }
                            }
                        });
                    } else {
                        searchView.setText("");
                    }
                }
            }
        });


        MaterialTextView openShop = view.findViewById(R.id.textTime);
        openShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CafeViewModel cafeViewModel = new ViewModelProvider(getActivity()).get(CafeViewModel.class);
                cafeViewModel.getCafeByOpen().observe(getViewLifecycleOwner(), new Observer<List<CafeEntity>>() {
                    @Override
                    public void onChanged(List<CafeEntity> cafeEntities) {
                        if(!cafeEntities.isEmpty()){
                            List<Cafe> cafe = new ArrayList<>();
                            List<Cafe> cafes= new ArrayList<>();

                            for(CafeEntity cafeEntity : cafeEntities){
                                Cafe cafe2 = Converter.convertCafeEntityToCafeModel(cafeEntity);
                                cafes.add(cafe2);
                            }

                            updateRecycleViewOrther(view,cafes);
                            updateRecycleViewFindCafe(view,cafe);
                        }
                    }
                });
            }
        });

        return view;
    }


    private void updateRecycleViewOrther(View view, List<Cafe> cafes) {
        RecyclerView recyclerViewOther = view.findViewById(R.id.recyclerViewCafeByOrther);
        recyclerViewOther.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewOther.setAdapter(new CafeAdapter(cafes, new RecyclerViewOnClickItem() {
            @Override
            public void onItemClickCafe(Cafe cafe) {
                Intent intent = new Intent(getContext(), DetailAcitvity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Object_Cafe", cafe);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }));
    }


    private void updateRecycleViewFindCafe(View view, List<Cafe> cafes) {
        RecyclerView recycleView = view.findViewById(R.id.recyclerViewCafe);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.setAdapter(new CafeAdapter(cafes, new RecyclerViewOnClickItem() {
            @Override
            public void onItemClickCafe(Cafe cafe) {
                Intent intent = new Intent(getContext(), DetailAcitvity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Object_Cafe", cafe);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }));
    }

    private void openDialogPrice(int gravity) {
        Context context = getActivity();
        if (context == null) {
            return;
        }
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_price);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributesribute = window.getAttributes();
        windowAttributesribute.gravity = gravity;
        window.setAttributes(windowAttributesribute);

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        TextView resetFilter = dialog.findViewById(R.id.resetFilter);
        TextView closeFilter = dialog.findViewById(R.id.closeFilter);

        //price
        CheckBox cBoxPriceCheap = dialog.findViewById(R.id.cBoxPriceCheap);
        CheckBox cBoxPriceMedium = dialog.findViewById(R.id.cBoxPriceMedium);
        CheckBox cBoxPriceHigh = dialog.findViewById(R.id.cBoxPriceHigh);

        SeekBar seekBarPrice = dialog.findViewById(R.id.seekBarPrice);
        TextView textViewProgressPrice = dialog.findViewById(R.id.textViewProgressPrice);

        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBarPrice, int progress, boolean fromUser) {
                if (cBoxPriceCheap.isChecked()) {
                    seekBarPrice.setProgress(50);
                    textViewProgressPrice.setText("50k");
                } else {
                    textViewProgressPrice.setText(progress + "k");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBarPrice) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBarPrice) {
            }
        });

        cBoxPriceCheap.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cBoxPriceMedium.setChecked(false);
                cBoxPriceHigh.setChecked(false);
                seekBarPrice.setProgress(30);
                textViewProgressPrice.setText("30k");
            } else {
                int progress = seekBarPrice.getProgress();
                textViewProgressPrice.setText(progress + "k");
            }
        });
        cBoxPriceMedium.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cBoxPriceCheap.setChecked(false);
                cBoxPriceHigh.setChecked(false);
                seekBarPrice.setProgress(60);
                textViewProgressPrice.setText("60k");
            } else {
                int progress = seekBarPrice.getProgress();
                textViewProgressPrice.setText(progress + "k");
            }
        });

        cBoxPriceHigh.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cBoxPriceCheap.setChecked(false);
                cBoxPriceMedium.setChecked(false);
                seekBarPrice.setProgress(100);
                textViewProgressPrice.setText("100k");
            } else {
                int progress = seekBarPrice.getProgress();
                textViewProgressPrice.setText(progress + "k");
            }
        });

        resetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBarPrice.setProgress(70);
                textViewProgressPrice.setText("70k");
                cBoxPriceCheap.setChecked(false);
                cBoxPriceMedium.setChecked(false);
                cBoxPriceHigh.setChecked(false);
            }
        });
        closeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button searchClick = dialog.findViewById(R.id.apply);
        searchClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price = String.valueOf(seekBarPrice.getProgress());
                CafeViewModel cafeViewModel = new ViewModelProvider(getActivity()).get(CafeViewModel.class);
                cafeViewModel.getCafeByPrice(price).observe(getViewLifecycleOwner(), new Observer<List<CafeEntity>>() {
                    @Override
                    public void onChanged(List<CafeEntity> cafeEntities) {
                        if(!cafeEntities.isEmpty()){
                            List<Cafe> cafes = new ArrayList<>();
                            for(CafeEntity cafeEntity : cafeEntities){
                                Cafe cafe = Converter.convertCafeEntityToCafeModel(cafeEntity);
                                cafes.add(cafe);
                            }
                            View view = getView();
                            updateRecycleViewOrther(view,cafes);

                            List<Cafe> cafe= new ArrayList<>();
                            updateRecycleViewFindCafe(view,cafe);
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();
    }

    private void openDialogDistance(int gravity) {
        Context context = getActivity();
        if (context == null) {
            return;
        }
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_distance);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributesribute = window.getAttributes();
        windowAttributesribute.gravity = gravity;
        window.setAttributes(windowAttributesribute);

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);

        }
        TextView resetFilter = dialog.findViewById(R.id.resetFilter);
        TextView closeFilter = dialog.findViewById(R.id.closeFilter);
        //distance
        CheckBox cBoxDisWalk = dialog.findViewById(R.id.cBoxDisWalk);
        CheckBox cBoxDisMedium = dialog.findViewById(R.id.cBoxDisMedium);
        CheckBox cBoxDisFar = dialog.findViewById(R.id.cBoxDisFar);

        SeekBar seekBarDistance = dialog.findViewById(R.id.seekBarDistance);
        TextView textViewProgressDistance = dialog.findViewById(R.id.textViewProgressDistance);
        cBoxDisWalk.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cBoxDisMedium.setChecked(false);
                cBoxDisFar.setChecked(false);
                seekBarDistance.setProgress(1);
                textViewProgressDistance.setText("1km");
            } else {
                int progress = seekBarDistance.getProgress();
                textViewProgressDistance.setText(progress + "km");
            }
        });

        cBoxDisMedium.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cBoxDisWalk.setChecked(false);
                cBoxDisFar.setChecked(false);
                seekBarDistance.setProgress(7);
                textViewProgressDistance.setText("7km");
            } else {
                int progress = seekBarDistance.getProgress();
                textViewProgressDistance.setText(progress + "km");
            }
        });

        cBoxDisFar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cBoxDisWalk.setChecked(false);
                cBoxDisMedium.setChecked(false);
                seekBarDistance.setProgress(20);
                textViewProgressDistance.setText("20km");
            } else {
                int progress = seekBarDistance.getProgress();
                textViewProgressDistance.setText(progress + "km");
            }
        });

        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBarPrice, int progress, boolean fromUser) {
                textViewProgressDistance.setText(progress + "km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBarPrice) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBarPrice) {
            }
        });
        resetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cBoxDisWalk.setChecked(false);
                cBoxDisMedium.setChecked(false);
                cBoxDisFar.setChecked(false);
                seekBarDistance.setProgress(15);
                textViewProgressDistance.setText("10km");

            }
        });
        closeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button searchClick= dialog.findViewById(R.id.apply);
        searchClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String distance = String.valueOf(seekBarDistance.getProgress());
                CafeViewModel cafeViewModel = new ViewModelProvider(getActivity()).get(CafeViewModel.class);
                cafeViewModel.getCafeByDistance(distance).observe(getViewLifecycleOwner(), new Observer<List<CafeEntity>>() {
                    @Override
                    public void onChanged(List<CafeEntity> cafeEntities) {
                        if(!cafeEntities.isEmpty()){
                            List<Cafe> cafes = new ArrayList<>();
                            for(CafeEntity cafeEntity : cafeEntities){
                                Cafe cafe = Converter.convertCafeEntityToCafeModel(cafeEntity);
                                cafes.add(cafe);
                            }
                            View view = getView();
                            updateRecycleViewOrther(view,cafes);

                            List<Cafe> cafe= new ArrayList<>();
                            updateRecycleViewFindCafe(view,cafe);
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();

    }


    private void openDialogPurpose(int gravity) {
        Context context = getActivity();
        if (context == null) {
            return;
        }
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_purpose);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributesribute = window.getAttributes();
        windowAttributesribute.gravity = gravity;
        window.setAttributes(windowAttributesribute);

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);

        }
        TextView resetFilter = dialog.findViewById(R.id.resetFilter);
        TextView closeFilter = dialog.findViewById(R.id.closeFilter);

        //checkbox
        CheckBox cBoxAlone = dialog.findViewById(R.id.cBoxAlone);
        CheckBox cBoxDate = dialog.findViewById(R.id.cBoxDate);
        CheckBox cBoxDrink = dialog.findViewById(R.id.cBoxDrink);
        CheckBox cBoxSelfie = dialog.findViewById(R.id.cBoxSelfie);
        CheckBox cBoxReadBook = dialog.findViewById(R.id.cBoxReadBook);
        CheckBox cBoxFriends = dialog.findViewById(R.id.cBoxFriends);
        CheckBox cBoxBar = dialog.findViewById(R.id.cBoxBar);
        CheckBox cBoxTime = dialog.findViewById(R.id.cBoxTime);
        CheckBox cBoxWork = dialog.findViewById(R.id.cBoxWork);
        resetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cBoxAlone.setChecked(false);
                cBoxDate.setChecked(false);
                cBoxDrink.setChecked(false);
                cBoxSelfie.setChecked(false);
                cBoxReadBook.setChecked(false);
                cBoxFriends.setChecked(false);
                cBoxBar.setChecked(false);
                cBoxTime.setChecked(false);
                cBoxWork.setChecked(false);

            }
        });
        closeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button searchClick = dialog.findViewById(R.id.apply);
        searchClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> purposes = new ArrayList<>();
                if (cBoxAlone.isChecked()) {
                    purposes.add("1 mình");
                }
                if (cBoxDate.isChecked()) {
                    purposes.add("Hẹn hò");
                }
                if (cBoxDrink.isChecked()) {
                    purposes.add("Đồ uống ngon");
                }
                if (cBoxSelfie.isChecked()) {
                    purposes.add("Sống ảo");
                }
                if (cBoxReadBook.isChecked()) {
                    purposes.add("Đọc sách");
                }
                if (cBoxFriends.isChecked()) {
                    purposes.add("Bạn bè");
                }
                if (cBoxBar.isChecked()) {
                    purposes.add("Bar/Pub");
                }
                if (cBoxTime.isChecked()) {
                    purposes.add("Mở muộn");
                }
                if (cBoxWork.isChecked()) {
                    purposes.add("Làm việc");
                }
                if(!purposes.isEmpty()){
                    CafeViewModel cafeViewModel = new ViewModelProvider(getActivity()).get(CafeViewModel.class);
                    cafeViewModel.getCafeByPurpose(purposes).observe(getViewLifecycleOwner(), new Observer<List<CafeEntity>>() {
                        @Override
                        public void onChanged(List<CafeEntity> cafeEntities) {
                            List<Cafe> cafes = new ArrayList<>();
                            for(CafeEntity cafeEntity:cafeEntities){
                                Cafe cafe = Converter.convertCafeEntityToCafeModel(cafeEntity);
                                cafes.add(cafe);
                            }
                            List<Cafe> cafe = new ArrayList<>();
                            View view = getView();
                            updateRecycleViewFindCafe(view,cafe);
                            updateRecycleViewOrther(view,cafes);
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        dialog.show();

    }


    private void openDialogFilter(int gravity) {

        Context context = getActivity();
        if (context == null) {
            return;
        }
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_filter);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }


        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributesribute = window.getAttributes();
        windowAttributesribute.gravity = gravity;
        window.setAttributes(windowAttributesribute);

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);

        }
        TextView resetFilter = dialog.findViewById(R.id.resetFilter);
        TextView closeFilter = dialog.findViewById(R.id.closeFilter);

        //option
        CheckBox checkBox = dialog.findViewById(R.id.checkBox);
        RadioButton nearlyRadioBtn = dialog.findViewById(R.id.nearlyRadioBtn);
        RadioButton cheapestRadioBtn = dialog.findViewById(R.id.cheapestRadioBtn);
        RadioButton expensiveRadioBtn = dialog.findViewById(R.id.expensiveRadioBtn);

        //purpose
        CheckBox cBoxAlone = dialog.findViewById(R.id.cBoxAlone);
        CheckBox cBoxDate = dialog.findViewById(R.id.cBoxDate);
        CheckBox cBoxDrink = dialog.findViewById(R.id.cBoxDrink);
        CheckBox cBoxSelfie = dialog.findViewById(R.id.cBoxSelfie);
        CheckBox cBoxReadBook = dialog.findViewById(R.id.cBoxReadBook);
        CheckBox cBoxFriends = dialog.findViewById(R.id.cBoxFriends);
        CheckBox cBoxBar = dialog.findViewById(R.id.cBoxBar);
        CheckBox cBoxTime = dialog.findViewById(R.id.cBoxTime);
        CheckBox cBoxWork = dialog.findViewById(R.id.cBoxWork);


        //distance
        CheckBox cBoxDisWalk = dialog.findViewById(R.id.cBoxDisWalk);
        CheckBox cBoxDisMedium = dialog.findViewById(R.id.cBoxDisMedium);
        CheckBox cBoxDisFar = dialog.findViewById(R.id.cBoxDisFar);

        SeekBar seekBarDistance = dialog.findViewById(R.id.seekBarDistance);
        TextView textViewProgressDistance = dialog.findViewById(R.id.textViewProgressDistance);

        textViewProgressDistance.setText(seekBarDistance.getProgress() + "km");

        //price
        SeekBar seekBarPrice = dialog.findViewById(R.id.seekBarPrice);
        TextView textViewProgressPrice = dialog.findViewById(R.id.textViewProgressPrice);
        textViewProgressPrice.setText(seekBarPrice.getProgress() + "k");


        CheckBox cBoxPriceCheap = dialog.findViewById(R.id.cBoxPriceCheap);
        CheckBox cBoxPriceMedium = dialog.findViewById(R.id.cBoxPriceMedium);
        CheckBox cBoxPriceHigh = dialog.findViewById(R.id.cBoxPriceHigh);


        // Prices
        cBoxPriceCheap.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cBoxPriceMedium.setChecked(false);
                cBoxPriceHigh.setChecked(false);
                seekBarPrice.setProgress(30);
                textViewProgressPrice.setText("30k");
            } else {
                int progress = seekBarPrice.getProgress();
                textViewProgressPrice.setText(progress + "k");
            }
        });
        cBoxPriceMedium.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cBoxPriceCheap.setChecked(false);
                cBoxPriceHigh.setChecked(false);
                seekBarPrice.setProgress(60);
                textViewProgressPrice.setText("60k");
            } else {
                int progress = seekBarPrice.getProgress();
                textViewProgressPrice.setText(progress + "k");
            }
        });

        cBoxPriceHigh.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cBoxPriceCheap.setChecked(false);
                cBoxPriceMedium.setChecked(false);
                seekBarPrice.setProgress(100);
                textViewProgressPrice.setText("100k");
            } else {
                int progress = seekBarPrice.getProgress();
                textViewProgressPrice.setText(progress + "k");
            }
        });
        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBarPrice, int progress, boolean fromUser) {
                textViewProgressPrice.setText(progress + "k");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBarPrice) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBarPrice) {
            }
        });


        //Distance
        cBoxDisWalk.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cBoxDisMedium.setChecked(false);
                cBoxDisFar.setChecked(false);
                seekBarDistance.setProgress(1);
                textViewProgressDistance.setText("1km");
            } else {
                int progress = seekBarDistance.getProgress();
                textViewProgressDistance.setText(progress + "km");
            }
        });

        cBoxDisMedium.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cBoxDisWalk.setChecked(false);
                cBoxDisFar.setChecked(false);
                seekBarDistance.setProgress(7);
                textViewProgressDistance.setText("7km");
            } else {
                int progress = seekBarDistance.getProgress();
                textViewProgressDistance.setText(progress + "km");
            }
        });

        cBoxDisFar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cBoxDisWalk.setChecked(false);
                cBoxDisMedium.setChecked(false);
                seekBarDistance.setProgress(20);
                textViewProgressDistance.setText("20km");
            } else {
                int progress = seekBarDistance.getProgress();
                textViewProgressDistance.setText(progress + "km");
            }
        });

        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBarPrice, int progress, boolean fromUser) {
                textViewProgressDistance.setText(progress + "km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBarPrice) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBarPrice) {
            }
        });
        resetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(false);

                nearlyRadioBtn.setChecked(false);
                cheapestRadioBtn.setChecked(false);
                expensiveRadioBtn.setChecked(false);

                cBoxAlone.setChecked(false);
                cBoxDate.setChecked(false);
                cBoxDrink.setChecked(false);
                cBoxSelfie.setChecked(false);
                cBoxReadBook.setChecked(false);
                cBoxFriends.setChecked(false);
                cBoxBar.setChecked(false);
                cBoxTime.setChecked(false);
                cBoxWork.setChecked(false);

                cBoxDisWalk.setChecked(false);
                cBoxDisMedium.setChecked(false);
                cBoxDisFar.setChecked(false);

                seekBarDistance.setProgress(15);
                textViewProgressDistance.setText("10km");

                seekBarPrice.setProgress(70);
                textViewProgressPrice.setText("70k");

                cBoxPriceCheap.setChecked(false);
                cBoxPriceMedium.setChecked(false);
                cBoxPriceHigh.setChecked(false);

            }
        });
        closeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button clickSearch = dialog.findViewById(R.id.apply);
        clickSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = null;
                if (checkBox.isChecked()) {
                    time = "time";
                }


                List<String> orders = new ArrayList<>();
                if (nearlyRadioBtn.isChecked()) {
                    orders.add("distance");
                } else if (cheapestRadioBtn.isChecked()) {
                    orders.add("cheap_price");
                } else if (expensiveRadioBtn.isChecked()) {
                    orders.add("expensive_price");
                }


                List<String> purposes = new ArrayList<>();
                if (cBoxAlone.isChecked()) {
                    purposes.add("1 mình");
                }
                if (cBoxDate.isChecked()) {
                    purposes.add("Hẹn hò");
                }
                if (cBoxDrink.isChecked()) {
                    purposes.add("Đồ uống ngon");
                }
                if (cBoxSelfie.isChecked()) {
                    purposes.add("Sống ảo");
                }
                if (cBoxReadBook.isChecked()) {
                    purposes.add("Đọc sách");
                }
                if (cBoxFriends.isChecked()) {
                    purposes.add("Bạn bè");
                }
                if (cBoxBar.isChecked()) {
                    purposes.add("Bar/Pub");
                }
                if (cBoxTime.isChecked()) {
                    purposes.add("Mở muộn");
                }
                if (cBoxWork.isChecked()) {
                    purposes.add("Làm việc");
                }

                String distance = String.valueOf(seekBarDistance.getProgress());
                String price = String.valueOf(seekBarPrice.getProgress());

                CafeViewModel cafeViewModel = new ViewModelProvider(getActivity()).get(CafeViewModel.class);
                cafeViewModel.getCafesOrderBy(orders, time, purposes, distance, price).observe(getViewLifecycleOwner(), new Observer<List<CafeEntity>>() {
                    @Override
                    public void onChanged(List<CafeEntity> cafeEntities) {
                        if (!cafeEntities.isEmpty()) {
                            List<Cafe> cafes = new ArrayList<>();
                            for (CafeEntity cafeEntity : cafeEntities) {
                                Cafe cafe = new Cafe();
                                cafe = Converter.convertCafeEntityToCafeModel(cafeEntity);
                                cafes.add(cafe);
                            }
                            Log.d("List cafe search", String.valueOf(cafeEntities.size()));
                            View view = getView();
                            updateRecycleViewOrther(view,cafes);

                            List<Cafe> cafe = new ArrayList<>();
                            updateRecycleViewFindCafe(view,cafe);
                        } else {
                            Toast.makeText(context, "Có vẻ là bạn chưa chọn bộ lọc !", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();

    }
}