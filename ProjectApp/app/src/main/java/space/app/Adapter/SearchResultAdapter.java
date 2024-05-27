package space.app.Adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import space.app.Activity.SearchAcitivity;
import space.app.Database.Entity.SearchResultEntity;
import space.app.Interface.RecyclerViewOnClickItem;
import space.app.Interface.RecyclerViewSearchOnClickItem;
import space.app.R;
import space.app.ViewModel.SearchViewModel;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private ArrayList<SearchResultEntity> searchResultEntities;
    private RecyclerViewSearchOnClickItem clickItem;

    public SearchResultAdapter(ArrayList<SearchResultEntity> searchResultEntities, RecyclerViewSearchOnClickItem clickItem) {
        this.searchResultEntities = searchResultEntities;
        this.clickItem = clickItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.research_guess_compo, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.ViewHolder holder, int position) {
        SearchResultEntity searchResult = searchResultEntities.get(position);
        holder.bind(searchResult);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickItem != null) {
                    clickItem.onItemClickSearch(searchResult);
                }
            }
        });
        holder.closeResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchResultEntities.remove(position);
                notifyItemRemoved(position);
                SearchAcitivity activity = (SearchAcitivity) holder.itemView.getContext();
                SearchViewModel searchViewModel = new ViewModelProvider(activity).get(SearchViewModel.class);
                searchViewModel.deleteSearchResultWithQuery(searchResult.getSearchQuery());
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResultEntities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textSearch;
        private ImageView closeResearch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textSearch = itemView.findViewById(R.id.textView12);
            this.closeResearch = itemView.findViewById(R.id.closeResearch);
        }

        public void bind(SearchResultEntity searchResult) {
            textSearch.setText(searchResult.getSearchQuery());
        }
    }
}
