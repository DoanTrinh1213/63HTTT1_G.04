package space.app.UI.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import space.app.Activity.MainActivity;
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
        showAlertDialog();

    }

    private void showAlertDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        // edit information
        LinearLayout lnEditInformation = view.findViewById(R.id.lnEditInformation);
        lnEditInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new FragmentEditInformation(), true);
            }
        });
//        // CafeContribute
//        ImageView arrowCafeContribute = view.findViewById(R.id.arrowCafeContribute);
//        arrowCafeContribute.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity) getActivity()).replaceFragment(new FragmentCafeContribute(), true);
//            }
//        });

        // ContributeCafeInformation
        LinearLayout lnContributeCafeInformation = view.findViewById(R.id.lnContributeCafeInformation);
        lnContributeCafeInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new FragmentContributeCafeInformation(), true);
            }
        });
        // DeleteAcount
        LinearLayout lnDeleteAcount = view.findViewById(R.id.lnDeleteAcount);
        lnDeleteAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new FragmentDeleteAcount(), true);
            }
        });
//
//        private void showAlertDialog() {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
////            builder.setTitle("Thông báo");
//            builder.setMessage("Bạn muốn xóa tài khoản");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // Người dùng nhấn Hủy
//                    dialog.dismiss();
//                }
//            });
//
//            AlertDialog dialog = builder.create();
//            dialog.show();
//        }
//

        
        // Contact
        LinearLayout lnContact = view.findViewById(R.id.lnContact);
        lnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new FragmentContact(), true);
            }
        });
        // InformationApp
        LinearLayout lnInformationApp = view.findViewById(R.id.lnInformationApp);
        lnInformationApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new FragmentInformationApp(), true);
            }
        });
//        // EvaluateApp
//        LinearLayout lnApp = view.findViewById(R.id.lnApp);
//        lnApp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity) getActivity()).replaceFragment(new FragmentInformationApp(), true);
//            }
//        });
        // LogOut
        LinearLayout lnLogOut = view.findViewById(R.id.lnLogOut);
        lnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new FragmentLogin(), true);
            }
        });

        return view;
    }
}
