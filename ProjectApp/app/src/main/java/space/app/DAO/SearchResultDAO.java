package space.app.DAO;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import space.app.Database.Entity.SearchResultEntity;

@Dao
public interface SearchResultDAO {
    @Query("SELECT * FROM searchResult")
    LiveData<List<SearchResultEntity>> getAllSearch();
    @Insert
    void insertSearchResult(SearchResultEntity searchResult);
    @Query("DELETE FROM searchResult")
    void deleteAll();
    @Delete
    void deleteSearchResult(SearchResultEntity searchResult);
}
