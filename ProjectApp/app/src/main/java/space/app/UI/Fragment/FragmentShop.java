package space.app.UI.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import space.app.Activity.MainActivity;
import space.app.Activity.ReviewAllActivity;
import space.app.Activity.RewriteActivity;
import space.app.Database.Entity.UriEntity;
import space.app.Model.Cafe;
import space.app.Model.User;
import space.app.R;
import space.app.ViewModel.UriViewModel;

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
    private Cafe cafe;
    private LinearLayout tick;

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
        cafe = new Cafe();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            cafe = (Cafe) getArguments().getSerializable("Object_Cafe");
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
        LinearLayout tick = view.findViewById(R.id.tick);
        Button ViewAll = view.findViewById(R.id.ViewAll);
        Button btnContribute = view.findViewById(R.id.btnCafeContribute);
        TextView nameCafe = view.findViewById(R.id.nameCafe);
        nameCafe.setText(cafe.getResName());

        ImageView imageShop = view.findViewById(R.id.imageShop);
        Button btnImage = view.findViewById(R.id.btnImage);
        TextView address = view.findViewById(R.id.address);
        TextView open = view.findViewById(R.id.open);
        TextView timeOpen = view.findViewById(R.id.timeOpen);

        ImageView imageUser = view.findViewById(R.id.userImage);
        LinearLayout description = view.findViewById(R.id.description);
        TextView star = view.findViewById(R.id.star);
        TextView count = view.findViewById(R.id.count);
        TextView timeOpen1 = view.findViewById(R.id.timeOpen1);

        TextView price = view.findViewById(R.id.price);
        price.setText(String.valueOf(cafe.getPrice()));

        TextView hotline = view.findViewById(R.id.hotline);
        hotline.setText(cafe.getContact());

        TextView purpose = view.findViewById(R.id.purpose);
        purpose.setText(cafe.getPurpose());

        description.removeAllViews();

        TextView catalog = new TextView(getContext());
        if (cafe.getDescribe() != null)
            catalog.setText(cafe.getDescribe());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // Width
                LinearLayout.LayoutParams.WRAP_CONTENT  // Height
        );
        params.setMargins(10, 5, 10, 5);
        catalog.setLayoutParams(params);
        catalog.setTextColor(Color.parseColor("#040203"));
        description.addView(catalog);

        TextView username = view.findViewById(R.id.username);
        TextView userid = view.findViewById(R.id.userid);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        if(cafe.getIdUser()!=null){
            firebaseDatabase.getReference("User").child(cafe.getIdUser()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = new User();
                    user = snapshot.getValue(User.class);
                    if(user!=null){
                        username.setText(user.getUserName());
                        userid.setText(user.getId());
                    }
                    else{
                        username.setText("NonIdentity");
                        userid.setText("NonIdentity");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        RequestOptions requestOptions = new RequestOptions()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        UriViewModel uriViewModel = new ViewModelProvider(getActivity()).get(UriViewModel.class);
        uriViewModel.getUriByIdCafeAndType(cafe.getIdCafe(), "Cafe").observe(getActivity(), new Observer<List<UriEntity>>() {
            @Override
            public void onChanged(List<UriEntity> uriEntities) {
                if (!uriEntities.isEmpty()) {
                    Glide.with(getContext())
                            .load(uriEntities.get(uriEntities.size() - 1).getUri())
                            .apply(requestOptions)
                            .into(imageShop);
                }
            }
        });

        uriViewModel.getUriByIdCafeAndType(cafe.getIdCafe(), "User").observe(getActivity(), new Observer<List<UriEntity>>() {
            @Override
            public void onChanged(List<UriEntity> uriEntities) {
                if (!uriEntities.isEmpty()) {
                    Log.d("Image uri", uriEntities.get(0).getUri().toString());
                    Glide.with(getContext())
                            .load(uriEntities.get(0).getUri())
                            .apply(requestOptions)
                            .into(imageUser);
                }
            }
        });

        if (cafe.getAddress() != null)
            address.setText(cafe.getAddress());


        if (cafe.getTimeOpen() != null) {
            timeOpen.setText(cafe.getTimeOpen());
            timeOpen1.setText(cafe.getTimeOpen());
            String timeShop = cafe.getTimeOpen();
            String[] parts = timeShop.split("-");
            String openTime = parts[0];
            String closeTime = parts[1];

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

            Calendar calendar = Calendar.getInstance();
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

            try {
                Date openDate = sdf.parse(openTime);
                Date closeDate = sdf.parse(closeTime);

                calendar.setTime(openDate);
                int startHour = calendar.get(Calendar.HOUR_OF_DAY);
                calendar.setTime(closeDate);
                int endHour = calendar.get(Calendar.HOUR_OF_DAY);

                if (closeDate.before(openDate)) {
                    calendar.add(Calendar.DATE, 1);
                    endHour = calendar.get(Calendar.HOUR_OF_DAY);
                }

                if (currentHour >= startHour && currentHour < endHour) {
                    open.setText("Đang mở cửa ");
                    open.setTextColor(Color.parseColor("#047709"));
                } else {
                    open.setText("Đã đóng cửa ");
                    open.setTextColor(Color.parseColor("#d55352"));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


        // Set click listeners
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đổi Fragment khi nhấn vào iconBack
                getActivity().finish();
            }
        });

        lnfindCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_shop, new FragmentFindCafe());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        ViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_shop, new FragmentReviewAll());
//                transaction.addToBackStack(null);
//                transaction.commit();
                Intent intent = new Intent(getActivity(), ReviewAllActivity.class);
                startActivity(intent);
            }

        });

        btnContribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RewriteActivity.class);
                startActivity(intent);
            }
        });
        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogTickCafe(Gravity.BOTTOM);
            }
        });

        return view;
    }

    private void openDialogTickCafe(int gravity) {
        Context context = getActivity();
        if (context == null) {
            return;
        }
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_tickcafe);
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
        Button btnContribute = dialog.findViewById(R.id.btnContribute);
        Button btnUnderstood = dialog.findViewById(R.id.btnUnderstood);
        btnContribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RewriteActivity.class);
                startActivity(intent);
            }
        });
        btnUnderstood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
