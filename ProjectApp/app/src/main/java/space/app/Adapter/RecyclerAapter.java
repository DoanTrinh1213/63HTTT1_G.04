package space.app.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import space.app.R;

public class RecyclerAapter extends RecyclerView.Adapter<RecyclerAapter.ViewHolder> {
    private ArrayList<Uri> uriArrayList;

    public RecyclerAapter(ArrayList<Uri> uriArrayList) {

        this.uriArrayList = uriArrayList;
    }

//    // Define the ViewHolder class
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public ImageView imageView;
//
//        public ViewHolder(View view) {
//            super(view);
//            imageView = view.findViewById(R.id.imageView);
//        }
//    }

    @NonNull
    @Override
    public RecyclerAapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAapter.ViewHolder holder, int position) {
//        // Bind data to the ViewHolder
//        Uri uri = uriArrayList.get(position);
//        holder.imageView.setImageURI(uri);
        holder.imageView.setImageURI(uriArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        // Return the total number of items
        return 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}

