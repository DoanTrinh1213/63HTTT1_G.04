package space.app.Adapter;

import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import space.app.Interface.RecyclerViewOnClickItem;
import space.app.Model.Cafe;
import space.app.R;

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
                .inflate(R.layout.view_cafe_compo, parent, false);
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

        public CafeViewHolder(@NonNull View itemView) {
            super(itemView);
            cafeNameTextView = itemView.findViewById(R.id.nameCafe);
            cafeAddressTextView = itemView.findViewById(R.id.addressCafe);
            imageCafe = itemView.findViewById(R.id.imageCafe);
            timeOpen = itemView.findViewById(R.id.timeCafe);
        }

        public void bind(Cafe cafe) {
            cafeNameTextView.setText(cafe.getIdCafe());
            cafeAddressTextView.setText(cafe.getAddress());
            imageCafe.setImageResource(R.drawable.images);
            timeOpen.setText(cafe.getTimeOpen());
        }
    }
}