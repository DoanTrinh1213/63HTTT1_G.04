package space.app.UI.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import space.app.Activity.CafeContributeActivity;
import space.app.Activity.ContributeCafeInformationActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import space.app.Activity.EditInformationActivity;
import space.app.Activity.MainActivity;
import space.app.Database.Entity.UserEntity;
import space.app.Database.DatabaseRoom;
import space.app.Helper.Utils;
import space.app.R;
import space.app.ViewModel.UserViewModel;

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
    private DatabaseRoom databaseRoom;
    private UserViewModel userViewModel;


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
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        databaseRoom = DatabaseRoom.getInstance(getActivity());
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
                Intent intent = new Intent(getActivity(), CafeContributeActivity.class);
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

        TextView username = view.findViewById(R.id.username);
        TextView userid = view.findViewById(R.id.userid);
        ImageView userImage = view.findViewById(R.id.userImage);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String idUser = Utils.hashEmail(mAuth.getCurrentUser().getEmail());
        if(mAuth.getCurrentUser().getEmail().equalsIgnoreCase("findCoffee2003@gmail.com")){
            idUser="findCoffee";
        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Uri imageUri = Uri.parse(sharedPreferences.getString("imageUrl", "imageUrl"));
        String name = sharedPreferences.getString("userName", "name");
        String id = sharedPreferences.getString("id", "id");
        username.setText(name);
        userid.setText(id);

        if (imageUri != null) {
            userImage.setImageURI(imageUri);
        }
//        userImage.setImageURI(imageUri);
        Log.d("Uri image", imageUri.toString());
        userViewModel.getUserById(idUser).observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                if (userEntity != null) {
                    String name = userEntity.getUsername();
                    String id = userEntity.getIdUser();
                    id = Utils.hash32b(id);
                    String imageUrl = userEntity.getImageUrl();
//                    String deString = userEntity.getDescribe();
                    if (imageUrl == null) {
                        Glide.with(FragmentMe.this).load(R.drawable.image).into(userImage);
                    } else {
                        userImage.setImageURI(Uri.parse(imageUrl));
                    }
                    username.setText(name);
                    userid.setText(id);
                }
            }
        });

        return view;
    }

    private void openDialogDeleteAcount(int gravity) {
        Context context = getActivity();
        if (context == null) {
            return;
        }
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_deleteacount);
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
        dialog.show();
        TextView txtXacNhan = dialog.findViewById(R.id.txtXacNhan);
        TextView txtHuy = dialog.findViewById(R.id.txtHuy);

        FirebaseAuth user = FirebaseAuth.getInstance();
        FirebaseUser currentUser = user.getCurrentUser();
        String idUser = Utils.hashEmail(currentUser.getEmail().toString());
        txtXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser.getEmail().equalsIgnoreCase("findCoffee2003@gmail.com")) {
                    Toast.makeText(context, "Không thể xóa tài khoản admin!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // xoa user
                user.signOut();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(),
                        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build());

                mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Revoke access

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("imageUrl", null);
                        editor.apply();
                        mGoogleSignInClient.revokeAccess().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                firebaseDatabase.getReference("User").child(idUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(context, "Xóa người dùng thành công!", Toast.LENGTH_SHORT).show();
                                                        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putBoolean("isLoggedIn", false);
                                                        editor.apply();

                                                        FirebaseStorage storage = FirebaseStorage.getInstance();
                                                        StorageReference storageReference = storage.getReference();
                                                        StorageReference userImageRef = storageReference.child("Img/users/" + idUser);
                                                        userImageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                                            @Override
                                                            public void onSuccess(ListResult listResult) {
                                                                // Lặp qua từng đối tượng và xóa chúng
                                                                for (StorageReference item : listResult.getItems()) {
                                                                    item.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Log.d("FirebaseStorage", "Deleted file: " + item.getName());
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Log.e("FirebaseStorage", "Error deleting file " + item.getName(), e);
                                                                        }
                                                                    });
                                                                }
                                                                // Log thông báo khi đã xóa thành công hết các đối tượng trong thư mục
                                                                Log.d("FirebaseStorage", "All files have been deleted successfully.");
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e("FirebaseStorage", "Error listing files", e);
                                                            }
                                                        });

                                                        Intent intent = new Intent(context, MainActivity.class);
                                                        startActivity(intent);
                                                    } else {
                                                        Toast.makeText(context, "Có lỗi xảy ra , vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        txtHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void logOutUser(Dialog dialog) {
        FirebaseAuth.getInstance().signOut();

        // Clear shared preferences
        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.putString("imageUrl", null);

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
        dialog.show();
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
    }
}
