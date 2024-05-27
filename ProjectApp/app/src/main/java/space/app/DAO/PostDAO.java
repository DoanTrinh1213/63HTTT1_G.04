package space.app.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import space.app.Database.Entity.PostEntity;
import space.app.Model.Post;

@Dao
public interface PostDAO {
    @Query("SELECT * FROM post where idCafe = :idCafe")
    LiveData<List<PostEntity>> getAllPostOfCafe(String idCafe);
}
