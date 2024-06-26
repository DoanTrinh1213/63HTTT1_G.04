package space.app.Adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import space.app.Interface.RecyclerViewOnClickItem;
import space.app.Model.Cafe;
import space.app.R;

public class SearchGuessAdapter extends RecyclerView.Adapter<SearchGuessAdapter.SearchViewHolder> {

    private ArrayList<Cafe> cafes;
    private RecyclerViewOnClickItem clickItem;
    public SearchGuessAdapter(ArrayList<Cafe> cafes,RecyclerViewOnClickItem clickItem){
        this.cafes = cafes;
        this.clickItem = clickItem;
    }
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_guess_compo,parent,false);
            return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Cafe cafe = cafes.get(position);
        holder.bind(cafe);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickItem != null) {
                    clickItem.onItemClickCafe(cafe);
                }
            }
        });

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        firebaseStorage.getReference().child(cafe.getImages()+"/CafeImages").listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {
                List<StorageReference> storageReferences = task.getResult().getItems();
                StorageReference first = storageReferences.get(0);
                first.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(!((Activity)holder.itemView.getContext()).isFinishing())
                            Glide.with(holder.itemView.getContext()).load(task.getResult()).into(holder.imageSearch);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }
    public static class SearchViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageSearch;
        private TextView nameCafe;
        private TextView distance;
        private TextView position;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            imageSearch = itemView.findViewById(R.id.imageGuessSearch);
            nameCafe = itemView.findViewById(R.id.nameCafeTextView);
            distance = itemView.findViewById(R.id.distanceGuess);
            position = itemView.findViewById(R.id.positionCafe);
        }
        public void bind(Cafe cafe){
            nameCafe.setText(cafe.getResName());
            distance.setText(cafe.getAddress());
            position.setText(cafe.getAddress());
        }
    }
}
