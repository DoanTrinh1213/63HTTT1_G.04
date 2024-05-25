package space.app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

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
