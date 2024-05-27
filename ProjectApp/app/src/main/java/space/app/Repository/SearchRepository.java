package space.app.Repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import space.app.DAO.SearchResultDAO;
import space.app.Database.Entity.SearchResultEntity;
import space.app.Database.SearchDatabase;

public class SearchRepository {
    private SearchResultDAO searchResultDao;
    private LiveData<List<SearchResultEntity>> allSearchResults;
    private ExecutorService databaseWriteExecutor;

    public SearchRepository(Application application) {
        SearchDatabase database = SearchDatabase.getInstance(application);
        searchResultDao = database.searchResultDao();
        allSearchResults = searchResultDao.getAllSearch();
        databaseWriteExecutor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<SearchResultEntity>> getAllSearch() {
        return allSearchResults;
    }
    public void insertSearchResult(SearchResultEntity searchResult) {
        databaseWriteExecutor.execute(() -> {
            searchResultDao.insertSearchResult(searchResult);
            Log.d("SearchRepository", "Inserted search result: " + searchResult.getSearchQuery());
        });
    }

    public void deleteSearchResult(SearchResultEntity searchResult) {
        databaseWriteExecutor.execute(() -> {
            searchResultDao.deleteSearchResult(searchResult);
        });
    }

    public void deleteSearchResultWithQuery(String query) {
        databaseWriteExecutor.execute(() -> {
            searchResultDao.deleteSearchResultWithQuery(query);
        });
    }
}
