package space.app.UI.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import space.app.Activity.DetailAcitvity;
import space.app.Adapter.CafeAdapter;
import space.app.Database.Entity.BookmarkEntity;
import space.app.Database.Entity.CafeEntity;
import space.app.Helper.Converter;
import space.app.Interface.RecyclerViewOnClickItem;
import space.app.Model.Cafe;
import space.app.R;
import space.app.ViewModel.BookmarkViewModel;
import space.app.ViewModel.CafeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentBookmark#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBookmark extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentBookmark() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentBookmark.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentBookmark newInstance(String param1, String param2) {
        FragmentBookmark fragment = new FragmentBookmark();
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
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        FrameLayout layoutmain = view.findViewById(R.id.mainBookmark);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("idUser", null);


        CafeViewModel cafeViewModel = new ViewModelProvider(getActivity()).get(CafeViewModel.class);
        BookmarkViewModel bookmarkViewModel = new ViewModelProvider(getActivity()).get(BookmarkViewModel.class);
        cafeViewModel.getAllCafeByBookmark(idUser).observe(getViewLifecycleOwner(), new Observer<List<CafeEntity>>() {
            @Override
            public void onChanged(List<CafeEntity> cafeEntities) {
                List<Cafe> cafes = new ArrayList<>();
                layoutmain.removeAllViews();
                if (!cafeEntities.isEmpty()) {
                    for (CafeEntity cafeEntity : cafeEntities) {
                        Cafe cafe = Converter.convertCafeEntityToCafeModel(cafeEntity);
                        cafes.add(cafe);
                    }
//                    Collections.sort(cafes, new Comparator<Cafe>() {
//                        @Override
//                        public int compare(Cafe cafe1, Cafe cafe2) {
//                            return Double.compare(Double.valueOf(cafe1.getEvaluate()), Double.valueOf(cafe2.getEvaluate()));
//                        }
//                    });
                    NestedScrollView nestedScrollView = new NestedScrollView(getContext());
                    nestedScrollView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    ));
                    RecyclerView recyclerView = getRecyclerView(cafes);
                    nestedScrollView.addView(recyclerView);
                    layoutmain.addView(nestedScrollView);
                } else {
                    LayoutInflater inflater1 = LayoutInflater.from(container.getContext());
                    View bookmarkNoneView = inflater.inflate(R.layout.bookmark_none_compo, null);
                    layoutmain.addView(bookmarkNoneView);
                }
            }
        });
//        int i=1;
//        if(i==0){
//            View layout1 = inflater.inflate(R.layout.bookmark_none_compo, (FrameLayout) view.findViewById(R.id.mainBookmark), true);
//        }
//        else{
//            LayoutInflater inflater1 = LayoutInflater.from(container.getContext());
//            View bookmarkNoneView = inflater.inflate(R.layout.bookmark_none_compo, null);
//            layoutmain.addView(bookmarkNoneView);
//        }
        return view;
    }

    @NonNull
    private RecyclerView getRecyclerView(List<Cafe> cafes) {
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setAdapter(new CafeAdapter(cafes, new RecyclerViewOnClickItem() {
            @Override
            public void onItemClickCafe(Cafe cafe) {
                Intent intent = new Intent(getContext(), DetailAcitvity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Object_Cafe", cafe);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }));
        return recyclerView;
    }
}