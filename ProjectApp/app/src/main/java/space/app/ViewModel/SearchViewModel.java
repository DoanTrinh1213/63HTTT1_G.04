package space.app.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import space.app.Database.Entity.SearchResultEntity;
import space.app.Repository.SearchRepository;

public class SearchViewModel extends AndroidViewModel {
    private SearchRepository repository;
    private LiveData<List<SearchResultEntity>> allSearchResults;
    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new SearchRepository(application);
        allSearchResults = repository.getAllSearch();
    }
    public LiveData<List<SearchResultEntity>> getAllSearchResults() {
        return allSearchResults;
    }

    public void insertSearchResult(SearchResultEntity searchResult) {
        repository.insertSearchResult(searchResult);
    }

    public void deleteSearchResult(SearchResultEntity searchResult) {
        repository.deleteSearchResult(searchResult);
    }

    public void deleteSearchResultWithQuery(String query){
        repository.deleteSearchResultWithQuery(query);
    }
}
