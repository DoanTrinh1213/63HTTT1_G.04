package space.app.UI.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import space.app.Activity.MainActivity;
import space.app.Helper.Utils;
import space.app.Model.User;
import space.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRegister#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRegister extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;
    private ActivityResultLauncher<Intent> signInLauncher;

    public FragmentRegister() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentRegister.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRegister newInstance(String param1, String param2) {
        FragmentRegister fragment = new FragmentRegister();
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
        mAuth = FirebaseAuth.getInstance();

        signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            handleSignInResult(task);
                        } else {
                            Log.w("Login", "Google sign-in canceled");
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        view.findViewById(R.id.textToLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new FragmentLogin(),true);
            }
        });

        EditText username = view.findViewById(R.id.usernameText);
        EditText password = view.findViewById(R.id.passwordText);

        Button registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(username, password);
            }
        });

        ImageButton imgbtn = view.findViewById(R.id.seePass);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
            }
        });
        TextView forgetTextView = view.findViewById(R.id.forgetPassword);
        forgetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogForgetPassword();
            }
        });
        
        return view;
    }
    private void openDialogForgetPassword() {
        Context context = getActivity();
        if (context == null) {
            return;
        } else {
            Dialog dialogfg = new Dialog(context);
            dialogfg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogfg.setContentView(R.layout.forgetpass_compo);
            Window window = dialogfg.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowAttributesribute = window.getAttributes();
            windowAttributesribute.gravity = Gravity.CENTER;
            window.setAttributes(windowAttributesribute);

            EditText email = dialogfg.findViewById(R.id.emailForget);
            Button btnForget = dialogfg.findViewById(R.id.sendMail);
            btnForget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (email.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Email không được để trống !", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(requireContext(), "Password reset email sent.", Toast.LENGTH_SHORT).show();
                                            dialogfg.dismiss();
                                        } else {
                                            Toast.makeText(requireContext(), "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            });
            dialogfg.show();
        }
    }

    private void registerUser(EditText username, EditText password) {
        String email = username.getText().toString();
        String pass = password.getText().toString();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(getActivity(), "Username and password cannot be empty", Toast.LENGTH_LONG).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Registration successful!", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    Toast.makeText(getActivity(), "Login successful!", Toast.LENGTH_SHORT).show();
                                    SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putBoolean("isLoggedIn", true);
                                    editor.apply();


                                    String idUser = Utils.hashEmail(email);
                                    User user1 = new User(idUser,email);

                                    FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
                                    firebaseDatabase.getReference("User").child(idUser).setValue(user1);

                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    getActivity().finish();

                                }
                            } else {
                                Log.w("Register", "User registration failed", task.getException());
                                Toast.makeText(getActivity(), "Registration failed! Please try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("Login", "Sign in with Google success");
                            } else {
                                Log.w("Login", "Sign in with Google failed", task.getException());
                                Toast.makeText(getActivity(), "Google sign-in failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (ApiException e) {
            Log.w("Login", "Google sign-in failed", e);
            Toast.makeText(getActivity(), "Google sign-in failed", Toast.LENGTH_SHORT).show();
        }
    }
}