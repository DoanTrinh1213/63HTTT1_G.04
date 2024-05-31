package space.app.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import space.app.R;

public class WriteReviewAdapter extends RecyclerView.Adapter<WriteReviewAdapter.ViewHolder> {

    private final ArrayList<Uri> uriList;
    private final Context context;
    CountOfImagesWhenRemoved countOfImagesWhenRemoved;
    private final  itemClickListener itemClickListener;

    public WriteReviewAdapter(ArrayList<Uri> uriList, Context context, CountOfImagesWhenRemoved countOfImagesWhenRemoved, itemClickListener itemClickListener) {
        this.uriList = uriList;
        this.context = context;
        this.countOfImagesWhenRemoved = countOfImagesWhenRemoved;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view, countOfImagesWhenRemoved, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        Uri uri = uriList.get(position);
//        Glide.with(holder.imageView.getContext()).load(uri).into(holder.imageView);
        Glide.with(context)
                .load(uriList.get(position))
                .into(holder.imageView);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uriList.remove(uriList.get(position));
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
                countOfImagesWhenRemoved.clicked(uriList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView, delete;
        CountOfImagesWhenRemoved countOfImagesWhenRemoved;
        itemClickListener itemClickListener;

        public ViewHolder(View itemView, CountOfImagesWhenRemoved countOfImagesWhenRemoved, itemClickListener itemClickListener) {
            super(itemView);
            this.countOfImagesWhenRemoved = countOfImagesWhenRemoved;
            imageView = itemView.findViewById(R.id.imageView);
            delete = itemView.findViewById(R.id.delete);

            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null){
                itemClickListener.itemClick(getAdapterPosition());
            }
        }
    }

    public  interface CountOfImagesWhenRemoved{
        void clicked(int getSize);
    }

    public interface  itemClickListener{
        void itemClick(int position);
    }
}
