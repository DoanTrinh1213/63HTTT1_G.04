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
    public void insertBookmark(BookmarkEntity bookmarkEntity);
    @Delete
    public void deleteBookmark(BookmarkEntity bookmarkEntity);

    @Query("Select * from bookmarks where idUser= :idUser")
    public LiveData<List<BookmarkEntity>> getAllBookmarkByIdUser(String idUser);

    @Query("Select * from bookmarks where idUser= :idUser and idCafe= :idCafe")
    LiveData<BookmarkEntity> getBookmarkByIdUserAndIdCafe(String idUser, String idCafe);
}
