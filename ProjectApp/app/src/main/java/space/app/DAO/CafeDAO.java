package space.app.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import space.app.Database.Entity.CafeEntity;
import space.app.Database.Entity.PostEntity;

@Dao
public interface CafeDAO {
    @Query("Select * from cafe")
    LiveData<List<CafeEntity>> getAllCafe();
    @Query("SELECT * FROM cafe WHERE resName LIKE '%' || :searchTerm || '%' and idUser!='findCoffee'")
    LiveData<List<CafeEntity>> getCafesBySearchTerm(String searchTerm);
    @Insert
    void insertCafe(CafeEntity cafe);

    @Query("Delete from cafe")
    void deleteAll();

    @Query("SELECT * FROM cafe ORDER BY evaluate DESC")
    LiveData<List<CafeEntity>> getAllCafesSortedByRating();

    @Query("SELECT * FROM cafe WHERE idCafe IN (SELECT idCafe FROM post WHERE idUser = :idUser and idCafe = :idCafe)")
    LiveData<List<CafeEntity>> getCafesPostedByUser(String idUser,String idCafe);

    @Query("SELECT * From cafe where idUser !='findCoffee' Order by evaluate desc")
    LiveData<List<CafeEntity>> getCafeByTopEvaluate();

    @Query("SELECT * From cafe where idUser = 'findCoffee' Order by evaluate desc limit 5")
    LiveData<List<CafeEntity>> getCafesByFindCoffee();

    @Query("SELECT * FROM cafe WHERE resName LIKE '%' || :searchTerm || '%' and idUser = 'findCoffee'" )
    LiveData<List<CafeEntity>> getCafesBySearchTermAndFindCoffee(String searchTerm);
    @Query("select * from cafe")
    LiveData<List<CafeEntity>> getAllCafe1();

    @Query("SELect * from cafe where resName = :resname")
    LiveData<List<CafeEntity>> getCafeByResName(String resname);
}
