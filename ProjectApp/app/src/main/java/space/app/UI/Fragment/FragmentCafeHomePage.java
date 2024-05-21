package space.app.UI.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import space.app.Activity.DetailAcitvity;
import space.app.Adapter.CafeAdapter;
import space.app.Interface.RecyclerViewOnClickItem;
import space.app.Model.Cafe;
import space.app.R;

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
        Cafe cafe = new Cafe("123", "CafePro1", "HaNoi", "Helloworld", (float) 20.3, "Menu", "13:00-24:00", "Hello", "URL", "5", "URL", "Sell");
        cafes.add(cafe);
        Cafe cafe2 = new Cafe("1234", "CafePro12", "HaNoi", "Helloworld", (float) 20.3, "Menu", "13:00-24:00", "Hello", "URL", "5", "URL", "Sell");
        cafes.add(cafe2);
        Cafe cafe3 = new Cafe("1235", "CafePro13", "HaNoi", "Helloworld", (float) 20.3, "Menu", "13:00-24:00", "Hello", "URL", "5", "URL", "Sell");
        cafes.add(cafe3);
        Cafe cafe4 = new Cafe("1236", "CafePro14", "HaNoi", "Helloworld", (float) 20.3, "Menu", "13:00-24:00", "Hello", "URL", "5", "URL", "Sell");
        cafes.add(cafe4);

        RecyclerView recycleView = view.findViewById(R.id.recyclerViewCafe);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.setAdapter(new CafeAdapter(cafes, new RecyclerViewOnClickItem() {
            @Override
            public void onItemClickCafe(Cafe cafe) {
//                Toast toast = Toast.makeText(getContext(), "You click in item!", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.TOP, 0, 0);
//                toast.show();
                Intent intent = new Intent(getContext(), DetailAcitvity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Object_Cafe",cafe);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }));

        RecyclerView recyclerViewOther = view.findViewById(R.id.recyclerViewCafeByOrther);
        recyclerViewOther.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewOther.setAdapter(new CafeAdapter(cafes, new RecyclerViewOnClickItem() {
            @Override
            public void onItemClickCafe(Cafe cafe) {


                Intent intent = new Intent(getContext(), DetailAcitvity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Object_Cafe",cafe);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }));

        return view;
    }
}