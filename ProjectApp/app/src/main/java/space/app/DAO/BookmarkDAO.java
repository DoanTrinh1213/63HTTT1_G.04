package space.app.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import space.app.Database.Entity.BookmarkEntity;

@Dao
public interface BookmarkDAO {
    @Insert
    void insertBookmark(BookmarkEntity bookmarkEntity);
    @Query("Delete from bookmarks where idCafe = :idCafe and idUser = :idUser")
    void deleteBookmark(String idCafe,String idUser);

    @Query("Delete from bookmarks")
    void deleteAllBookmark();

    @Query("Select * from bookmarks where idUser= :idUser")
    LiveData<List<BookmarkEntity>> getAllBookmarkByIdUser(String idUser);

    @Query("Select * from bookmarks where idUser= :idUser and idCafe= :idCafe")
    LiveData<BookmarkEntity> getBookmarkByIdUserAndIdCafe(String idUser, String idCafe);
}
