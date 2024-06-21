package space.app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import space.app.Activity.MainActivity;
import space.app.Database.Entity.BookmarkEntity;
import space.app.Interface.RecyclerViewOnClickItem;
import space.app.Model.Cafe;
import space.app.R;
import space.app.ViewModel.BookmarkViewModel;
import space.app.ViewModel.CafeViewModel;


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

        MainActivity mainActivity = (MainActivity) holder.itemView.getContext();
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("idUser", null);

        BookmarkViewModel bookmarkViewModel = new ViewModelProvider(mainActivity).get(BookmarkViewModel.class);
        bookmarkViewModel.getBookmarkByIdUserAndIdCafe(idUser, cafe.getIdCafe()).observe(mainActivity, new Observer<BookmarkEntity>() {
            @Override
            public void onChanged(BookmarkEntity bookmarkEntity) {
                if (bookmarkEntity != null) {
                    if(bookmarkEntity.getIdCafe().equals(cafe.getIdCafe()))
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

                BookmarkViewModel bookmarkViewModel = new ViewModelProvider(mainActivity).get(BookmarkViewModel.class);
                bookmarkViewModel.getBookmarkByIdUserAndIdCafe(idUser, cafe.getIdCafe()).observe(mainActivity, new Observer<BookmarkEntity>() {
                    @Override
                    public void onChanged(BookmarkEntity bookmarkEntity1) {
                        bookmarkViewModel.getBookmarkByIdUserAndIdCafe(idUser, cafe.getIdCafe()).removeObservers(mainActivity);

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
                                            for(DataSnapshot snap :snapshot.getChildren()){
                                                BookmarkEntity bookmark = snap.getValue(BookmarkEntity.class);
                                                if(bookmark.getIdCafe().equals(cafe.getIdCafe())){
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
                        },200);
                    }
                });
            }
        });

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

        public CafeViewHolder(@NonNull View itemView) {
            super(itemView);
            cafeNameTextView = itemView.findViewById(R.id.textViewCafeShop);
            cafeAddressTextView = itemView.findViewById(R.id.textViewCafeAdress);
            imageCafe = itemView.findViewById(R.id.imageViewCafe);
            timeOpen = itemView.findViewById(R.id.textViewCafeTimeOpen);
            bookmark = itemView.findViewById(R.id.iconBookmark);
        }

        public void bind(Cafe cafe) {
            cafeNameTextView.setText(cafe.getResName());
            cafeAddressTextView.setText(cafe.getAddress());
            imageCafe.setImageResource(R.drawable.images);
            timeOpen.setText(cafe.getTimeOpen());
        }
    }
}