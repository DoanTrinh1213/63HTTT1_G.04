package space.app.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import space.app.Database.Entity.CafeEntity;
import space.app.Database.Entity.SearchResultEntity;

@Dao
public interface CafeDAO {
    @Query("Select * from cafe")
    LiveData<List<CafeEntity>> getAllCafe();
    @Query("SELECT * FROM cafe WHERE resName LIKE '%' || :searchTerm || '%'")
    LiveData<List<CafeEntity>> getCafesBySearchTerm(String searchTerm);

    @Insert
    void insertCafe(CafeEntity cafe);

    @Query("Delete from cafe")
    void deleteAll();

    @Query("SELECT * FROM cafe ORDER BY evaluate DESC")
    LiveData<List<CafeEntity>> getAllCafesSortedByRating();
}
