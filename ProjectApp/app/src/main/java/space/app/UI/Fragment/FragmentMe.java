package space.app.UI.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import space.app.MainActivity;
import space.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMe extends Fragment {

    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentMe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMe.
     */
    // Rename and change types and number of parameters
    public static FragmentMe newInstance(String param1, String param2) {
        FragmentMe fragment = new FragmentMe();
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
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        // edit information
        ImageView arrowEditInformation = view.findViewById(R.id.arrowEditInformation);
        arrowEditInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new FragmentEditInformation(), true);
            }
        });
        // CafeContribute
//        ImageView arrowCafeContribute = view.findViewById(R.id.arrowCafeContribute);
//        arrowCafeContribute.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity) getActivity()).replaceFragment(new FragmentCafeContribute(), true);
//            }
//        });
        // ContributeCafeInformation
        ImageView arrowContributeCafeInformation = view.findViewById(R.id.arrowContributeCafeInformation);
        arrowContributeCafeInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new FragmentContributeCafeInformation(), true);
            }
        });
        // DeleteAcount
        ImageView arrowDeleteAcount = view.findViewById(R.id.arrowDeleteAcount);
        arrowDeleteAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new FragmentDeleteAcount(), true);
            }
        });
        // Contact
        ImageView arrowContact = view.findViewById(R.id.arrowContact);
        arrowContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new FragmentContact(), true);
            }
        });
        // InformationApp
        ImageView arrowInformationApp = view.findViewById(R.id.arrowInformationApp);
        arrowInformationApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new FragmentInformationApp(), true);
            }
        });
//        // EvaluateApp
//        ImageView arrowApp = view.findViewById(R.id.arrowApp);
//        arrowApp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity) getActivity()).replaceFragment(new FragmentInformationApp(), true);
//            }
//        });
        // LogOut
        ImageView arrowLogOut = view.findViewById(R.id.arrowLogOut);
        arrowLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new FragmentLogin(), true);
            }
        });

        return view;
    }
}
