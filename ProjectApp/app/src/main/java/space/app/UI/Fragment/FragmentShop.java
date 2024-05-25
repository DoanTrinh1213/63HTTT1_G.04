package space.app.UI.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import space.app.Activity.MainActivity;
import space.app.Activity.RewriteActivity;
import space.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentShop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentShop extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FragmentShop() {
        // Required empty public constructor
    }

    public static FragmentShop newInstance(String param1, String param2) {
        FragmentShop fragment = new FragmentShop();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        // Initialize UI elements
        ImageView iconBack = view.findViewById(R.id.iconBack);
        LinearLayout lnfindCafe = view.findViewById(R.id.lnfindCafe);
        Button ViewAll = view.findViewById(R.id.ViewAll);
        Button btnContribute = view.findViewById(R.id.btnCafeContribute);

        // Set click listeners
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new FragmentCafeHomePage(), true);
            }
        });

        lnfindCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new FragmentFindCafe(), true);
            }
        });

        ViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new FragmentReviewAll(), true);
            }
        });

        btnContribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start RewriteActivity
                Intent intent = new Intent(getActivity(), RewriteActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
