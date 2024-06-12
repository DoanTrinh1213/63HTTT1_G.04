package space.app.UI.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorBoundsInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import space.app.Activity.CafeContribute;
import space.app.Activity.ContributeCafeInformationActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import space.app.Activity.EditInformationActivity;
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
                Intent intent = new Intent(getActivity(), EditInformationActivity.class);
                startActivity(intent);
            }
        });
//        // CafeContribute

            LinearLayout lnCafeContribute = view.findViewById(R.id.lnCafeContribute);
            lnCafeContribute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CafeContribute.class);
                    startActivity(intent);
                }
            });

        // ContributeCafeInformation
        LinearLayout lnContributeCafeInformation = view.findViewById(R.id.lnContributeCafeInformation);
        lnContributeCafeInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContributeCafeInformationActivity.class);
                startActivity(intent);
            }
        });
        // DeleteAcount
        LinearLayout lnDeleteAcount = view.findViewById(R.id.lnDeleteAcount);
        lnDeleteAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogDeleteAcount(Gravity.CENTER);
            }
        });


        
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

        // LogOut
        LinearLayout lnLogOut = view.findViewById(R.id.lnLogOut);
        lnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogLogOut(Gravity.CENTER);
            }
        });

        return view;
    }

    private void openDialogDeleteAcount(int gravity) {
        Context context = getActivity();
        if(context==null){
            return;
        }
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_deleteacount);
        Window window = dialog.getWindow();
        if(window==null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT );
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributesribute = window.getAttributes();
        windowAttributesribute.gravity=gravity;
        window.setAttributes(windowAttributesribute);

        if (Gravity.CENTER==gravity){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);

        }
        TextView txtXacNhan= dialog.findViewById(R.id.txtXacNhan);
        TextView txtHuy= dialog.findViewById(R.id.txtHuy);
        txtXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        txtHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void logOutUser(Dialog dialog) {
        FirebaseAuth.getInstance().signOut();

        // Clear shared preferences
        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(),
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build());

        mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Revoke access
                mGoogleSignInClient.revokeAccess().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Redirect to login activity
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
            }
        });
    }

    private void openDialogLogOut(int gravity) {
        Context context = getActivity();
        if (context == null) {
            return;
        }
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_logout);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributesribute = window.getAttributes();
        windowAttributesribute.gravity = gravity;
        window.setAttributes(windowAttributesribute);

        if (Gravity.CENTER == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);

        }
        TextView txtConfirm = dialog.findViewById(R.id.txtConfirm);
        TextView txtCancle = dialog.findViewById(R.id.txtCancle);
        txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutUser(dialog);
            }
        });
        txtCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
