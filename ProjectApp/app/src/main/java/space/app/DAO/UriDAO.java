package space.app.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import space.app.Database.Entity.UriEntity;

@Dao
public interface UriDAO {
    @Insert
    void insertUri(UriEntity uri);
    @Delete
    void deleteUri(UriEntity uri);
    @Query("Select * from uri where idCafe =:idCafe and type =:type")
    LiveData<List<UriEntity>> getUriByIdCafeAndType(String idCafe,String type);

    @Query("Delete from uri")
    void deleteAllUri();
}
