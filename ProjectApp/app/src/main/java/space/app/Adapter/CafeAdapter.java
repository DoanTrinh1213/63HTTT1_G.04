package space.app.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import space.app.Activity.MainActivity;
import space.app.Database.Entity.BookmarkEntity;
import space.app.Database.Entity.UriEntity;
import space.app.Interface.RecyclerViewOnClickItem;
import space.app.Model.Cafe;
import space.app.R;
import space.app.ViewModel.BookmarkViewModel;
import space.app.ViewModel.CafeViewModel;
import space.app.ViewModel.UriViewModel;


public class CafeAdapter extends RecyclerView.Adapter<CafeAdapter.CafeViewHolder> {

    private List<Cafe> cafeList;
    private RecyclerViewOnClickItem clickItem;


    // Constructor
    public CafeAdapter(List<Cafe> cafeList, RecyclerViewOnClickItem recyclerViewOnClickItem) {
        this.cafeList = cafeList;
        this.clickItem = recyclerViewOnClickItem;
    }

    @NonNull
    @Override
    public CafeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_view_cafe, parent, false);
        return new CafeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CafeViewHolder holder, int position) {
        // Bind data to the item view
        Cafe cafe = cafeList.get(position);
        holder.bind(cafe);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickItem != null) {
                    clickItem.onItemClickCafe(cafe);
                }
            }
        });

        Context context = holder.itemView.getContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("idUser", null);

        BookmarkViewModel bookmarkViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(BookmarkViewModel.class);
        bookmarkViewModel.getBookmarkByIdUserAndIdCafe(idUser, cafe.getIdCafe()).observe((LifecycleOwner) context, new Observer<BookmarkEntity>() {
            @Override
            public void onChanged(BookmarkEntity bookmarkEntity) {
                if (bookmarkEntity != null) {
                    if (bookmarkEntity.getIdCafe().equals(cafe.getIdCafe()))
                        holder.bookmark.setImageResource(R.drawable.bookmark_love);
                }
            }
        });
        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BookmarkEntity bookmarkEntity = new BookmarkEntity();
                bookmarkEntity.setIdCafe(cafe.getIdCafe());
                bookmarkEntity.setIdUser(idUser);

                BookmarkViewModel bookmarkViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(BookmarkViewModel.class);
                bookmarkViewModel.getBookmarkByIdUserAndIdCafe(idUser, cafe.getIdCafe()).observe((LifecycleOwner) context, new Observer<BookmarkEntity>() {
                    @Override
                    public void onChanged(BookmarkEntity bookmarkEntity1) {
                        bookmarkViewModel.getBookmarkByIdUserAndIdCafe(idUser, cafe.getIdCafe()).removeObservers((LifecycleOwner) context);

                        FirebaseDatabase firebase = FirebaseDatabase.getInstance();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (bookmarkEntity1 == null) {
                                    Log.d("Add bookmark", "added");
                                    DatabaseReference data = firebase.getReference("Bookmark").child(idUser);
                                    data.push().setValue(bookmarkEntity);
                                    bookmarkViewModel.insertBookmark(bookmarkEntity);
                                    holder.bookmark.setImageResource(R.drawable.bookmark_love);
                                } else {
                                    DatabaseReference data = firebase.getReference("Bookmark").child(idUser);
                                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot snap : snapshot.getChildren()) {
                                                BookmarkEntity bookmark = snap.getValue(BookmarkEntity.class);
                                                if (bookmark.getIdCafe().equals(cafe.getIdCafe())) {
                                                    snap.getRef().removeValue();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    Log.d("Delete bookmark", "deleted");
                                    bookmarkViewModel.deleteBookmark(cafe.getIdCafe(), idUser);
                                    holder.bookmark.setImageResource(R.drawable.round_bookmark_border_24);
                                }
                            }
                        }, 200);
                    }
                });
            }
        });

        RequestOptions requestOptions = new RequestOptions()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        UriViewModel uriViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(UriViewModel.class);
        uriViewModel.getUriByIdCafeAndType(cafe.getIdCafe(), "Cafe").observe((LifecycleOwner) context, new Observer<List<UriEntity>>() {
            @Override
            public void onChanged(List<UriEntity> uriEntities) {
                if (!uriEntities.isEmpty()) {
                    Collections.sort(uriEntities, new Comparator<UriEntity>() {
                        @Override
                        public int compare(UriEntity o1, UriEntity o2) {
                            return o1.getUri().compareTo(o2.getUri());
                        }
                    });
                    Glide.with(context)
                            .load(uriEntities.get(0).getUri())
                            .apply(requestOptions)
                            .into(holder.imageCafe);
                }
            }
        });

        String timeShop = cafe.getTimeOpen();
        if (timeShop.equals("00:00-00:00")) {
            holder.open.setText("Đang mở cửa ");
            holder.open.setTextColor(Color.parseColor("#047709"));
        } else {
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
                    holder.open.setText("Đang mở cửa ");
                    holder.open.setTextColor(Color.parseColor("#047709"));
                } else {
                    holder.open.setText("Đã đóng cửa ");
                    holder.open.setTextColor(Color.parseColor("#d55352"));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        // Return the total number of items
        return cafeList.size();
    }

    // ViewHolder class to hold item views
    public static class CafeViewHolder extends RecyclerView.ViewHolder {
        private TextView cafeNameTextView;
        private TextView cafeAddressTextView;
        private ImageView imageCafe;
        private TextView timeOpen;
        private ImageView bookmark;

        private TextView open;

        public CafeViewHolder(@NonNull View itemView) {
            super(itemView);
            cafeNameTextView = itemView.findViewById(R.id.textViewCafeShop);
            cafeAddressTextView = itemView.findViewById(R.id.textViewCafeAdress);
            imageCafe = itemView.findViewById(R.id.imageViewCafe);
            timeOpen = itemView.findViewById(R.id.textViewCafeTimeOpen);
            bookmark = itemView.findViewById(R.id.iconBookmark);
            open = itemView.findViewById(R.id.textViewCafeTime);
        }

        public void bind(Cafe cafe) {
            cafeNameTextView.setText(cafe.getResName());
            cafeAddressTextView.setText(cafe.getAddress());
            imageCafe.setImageResource(R.drawable.images);
            timeOpen.setText(cafe.getTimeOpen());
        }
    }
}