package space.app.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import space.app.Interface.HeightWidthOnSet;
import space.app.Interface.RecyclerViewOnClickItem;
import space.app.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<Uri> uri;
    private Context context;
    private RecyclerViewOnClickItem recyclerViewOnClickItem;
    private HeightWidthOnSet heightWidthOnSet;

    public ImageAdapter(Context context, List<Uri> uri, RecyclerViewOnClickItem recyclerViewOnClickItem,HeightWidthOnSet heightWidthOnSet) {
        this.context = context;
        this.uri = uri;
        this.recyclerViewOnClickItem = recyclerViewOnClickItem;
        this.heightWidthOnSet = heightWidthOnSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_compo, parent, false);
        return new ViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uriImage = uri.get(position);
        if (uriImage != null) {
            holder.bind(uriImage);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewOnClickItem != null) {
                        recyclerViewOnClickItem.onItemClickImage(uriImage);
                    }
                }
            });
            if(heightWidthOnSet!=null){
                heightWidthOnSet.setParams(holder.image);
            }
        }
    }

    @Override
    public int getItemCount() {
        return uri.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private Context context;

        public ViewHolder(@NonNull View itemView, Context contextParent) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            context = contextParent;
        }

        public void bind(Uri uri) {
            Glide.with(context).load(uri).into(image);
        }
    }
}
