package space.app.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import space.app.R;

public class CafeImageRecyclerViewAdapter extends RecyclerView.Adapter<CafeImageRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Uri> imageUrls;
    private OnImageRemovedListener listener;

    public interface OnImageRemovedListener {
        void onImageRemoved();
    }

    public CafeImageRecyclerViewAdapter(Context context, ArrayList<Uri> imageUrls, OnImageRemovedListener listener) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_image_cafe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Uri uri = imageUrls.get(position);
        holder.UploadImageCafe.setImageURI(uri);

        holder.removeButtonCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUrls.size() > 3) {
                    imageUrls.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, imageUrls.size());
                    listener.onImageRemoved();
                    Toast.makeText(context, "Đã xóa ảnh thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Bạn cần up tối thiểu 3 ảnh", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView UploadImageCafe;
        ImageButton removeButtonCafe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            UploadImageCafe = itemView.findViewById(R.id.UploadImageCafe);
            removeButtonCafe = itemView.findViewById(R.id.removeButtonCafe);
        }
    }
}
