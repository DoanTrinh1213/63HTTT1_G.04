package space.app.UI.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import space.app.Activity.AllImageShowActivity;
import space.app.Activity.ContributeCafeInformationActivity;
import space.app.Activity.MainActivity;
import space.app.Activity.ReviewAllActivity;
import space.app.Activity.RewriteActivity;
import space.app.Adapter.ImageAdapter;
import space.app.Adapter.PostAdapter;
import space.app.Database.Entity.CafeEntity;
import space.app.Database.Entity.UriEntity;
import space.app.Helper.Converter;
import space.app.Helper.PostHelper;
import space.app.Interface.HeightWidthOnSet;
import space.app.Interface.RecyclerViewOnClickItem;
import space.app.Model.Cafe;
import space.app.Model.Post;
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
    private LinearLayout tick,linearRevieww;

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
        LinearLayout linearRevieww = view.findViewById(R.id.linearRevieww);
        Button ViewAll = view.findViewById(R.id.ViewAll);
        Button btnContribute = view.findViewById(R.id.btnCafeContribute);
        TextView nameCafe = view.findViewById(R.id.nameCafe);
        nameCafe.setText(cafe.getResName());

        ImageView imageShop = view.findViewById(R.id.imageShop);
        Button btnImage = view.findViewById(R.id.btnImage);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Không có ảnh để hiển thị!", Toast.LENGTH_SHORT).show();
            }
        });
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
        if (cafe.getIdUser() != null) {
            firebaseDatabase.getReference("User").child(cafe.getIdUser()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = new User();
                    user = snapshot.getValue(User.class);
                    if (user != null) {
                        username.setText(user.getUserName());
                        userid.setText(user.getId());
                    } else {
                        username.setText("NonIdentity");
                        userid.setText("NonIdentity");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        ArrayList<Uri> uriImageCafe = new ArrayList<>();

        RequestOptions requestOptions = new RequestOptions()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        UriViewModel uriViewModel = new ViewModelProvider(getActivity()).get(UriViewModel.class);
        uriViewModel.getUriByIdCafeAndType(cafe.getIdCafe(), "Cafe").observe(getActivity(), new Observer<List<UriEntity>>() {
            @Override
            public void onChanged(List<UriEntity> uriEntities) {
                if (!uriEntities.isEmpty()) {
                    Uri uri = Uri.parse(uriEntities.get(uriEntities.size() - 1).getUri());
                    if (getActivity() != null) {
                        Glide.with(getActivity())
                                .load(uri)
                                .apply(requestOptions)
                                .into(imageShop);
                    }

                    imageShop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDialogImage(uri);
                        }
                    });

                    uriImageCafe.clear();
                    for (UriEntity uriImage : uriEntities) {
                        uriImageCafe.add(Uri.parse(uriImage.getUri()));
                    }
                    btnImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), AllImageShowActivity.class);
                            intent.putExtra("ImageList", uriImageCafe);
                            intent.putExtra("nameCafe",cafe.getResName());
                            startActivity(intent);
                        }
                    });
                }
            }
        });

        uriViewModel.getUriByIdCafeAndType(cafe.getIdCafe(), "User").observe(getActivity(), new Observer<List<UriEntity>>() {
            @Override
            public void onChanged(List<UriEntity> uriEntities) {
                if (!uriEntities.isEmpty()) {
                    if (getActivity() != null) {
                        Log.d("Image uri", uriEntities.get(0).getUri().toString());
                        if (getActivity() != null) {
                            Glide.with(getActivity())
                                    .load(uriEntities.get(0).getUri())
                                    .apply(requestOptions)
                                    .into(imageUser);
                        }
                    }
                }
            }
        });

        if (cafe.getAddress() != null)
            address.setText(cafe.getAddress());


        if (cafe.getTimeOpen() != null) {
            if (cafe.getTimeOpen().equals("00:00-00:00")) {
                open.setText("Đang mở cửa ");
                open.setTextColor(Color.parseColor("#047709"));
            } else {
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

        }


        // Set click listeners
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đổi Fragment khi nhấn vào iconBack
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });

//        lnfindCafe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_shop, new FragmentFindCafe());
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });

        ViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReviewAllActivity.class);
                intent.putExtra("rewriteCafe",cafe);
                startActivity(intent);
            }

        });
        linearRevieww.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RewriteActivity.class);
                intent.putExtra("rewriteCafe",cafe);
                startActivity(intent);
            }
        });
        btnContribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RewriteActivity.class);
                intent.putExtra("rewriteCafe",cafe);
                startActivity(intent);
            }
        });
        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogTickCafe(Gravity.BOTTOM);
            }
        });

        RecyclerView menu = view.findViewById(R.id.menu);
        List<Uri> imgMenu = new ArrayList<>();
        uriViewModel.getUriByIdCafeAndType(cafe.getIdCafe(), "Menu").observe(getViewLifecycleOwner(), new Observer<List<UriEntity>>() {
            @Override
            public void onChanged(List<UriEntity> uriEntities) {
                if (uriEntities != null) {
                    imgMenu.clear();
                    for (UriEntity uri : uriEntities) {
                        imgMenu.add(Uri.parse(uri.getUri()));
                    }
                    menu.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    menu.setAdapter(new ImageAdapter(getContext(), imgMenu, new RecyclerViewOnClickItem() {
                        @Override
                        public void onItemClickImage(Uri uri) {
                            space.app.Helper.Dialog.openDialogImage(getContext(), uri);
                        }
                    }, new HeightWidthOnSet() {
                        @Override
                        public void setParams(View view) {
                            view.getLayoutParams().width = 300;
                            view.getLayoutParams().height = 300;
                        }
                    }));
                }
            }
        });

        FirebaseDatabase firebase = FirebaseDatabase.getInstance();
        firebase.getReference("Post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RecyclerView comments = view.findViewById(R.id.rcvReview);
                List<Post> postOfCafe = PostHelper.getInstance().getAllPostByIdCafe(cafe.getIdCafe());
                List<Post> postLimit = PostHelper.getInstance().getPostLimit5StarHigh(postOfCafe);
                comments.setAdapter(new PostAdapter(postLimit));
                comments.setLayoutManager(new LinearLayoutManager(getContext()));
                star.setText(String.valueOf(PostHelper.getInstance().starRating(cafe.getIdCafe())));
                count.setText(postOfCafe.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void openDialogImage(Uri uri) {
        Context context = getContext();
        if (context == null) {
            return;
        }
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_show_image_compo);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(0xBF000000));
        WindowManager.LayoutParams windowAttributesribute = window.getAttributes();
        windowAttributesribute.gravity = Gravity.CENTER;
        dialog.setCancelable(true);
        ImageView image = dialog.findViewById(R.id.image);
        if (uri != null) {
            Glide.with(context).load(uri).into(image);
        }
        dialog.show();
        ImageView backImg = dialog.findViewById(R.id.close);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
                Intent intent = new Intent(getActivity(), ContributeCafeInformationActivity.class);
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
