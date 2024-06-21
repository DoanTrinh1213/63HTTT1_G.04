package space.app.Repository;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.app.Application;


import androidx.lifecycle.LiveData;

import space.app.DAO.BookmarkDAO;
import space.app.Database.DatabaseRoom;
import space.app.Database.Entity.BookmarkEntity;

public class BookmarkRepository {
    private BookmarkDAO bookmarkDAO;
    private ExecutorService executorService;

    public BookmarkRepository(Application application){
        bookmarkDAO = DatabaseRoom.getInstance(application).bookmarkDAO();
        executorService = Executors.newSingleThreadExecutor();
    }
    public void insertBookmark(BookmarkEntity bookmarkEntity){
        executorService.execute(()->{
            bookmarkDAO.insertBookmark(bookmarkEntity);
        });
    }
    public void deleteBookmark(String idCafe,String idUser){
        executorService.execute(()->{
            bookmarkDAO.deleteBookmark(idCafe,idUser);
        });
    }

    public LiveData<List<BookmarkEntity>> getAllBookmarkByIduser(String idUser){
        return bookmarkDAO.getAllBookmarkByIdUser(idUser);
    }

    public LiveData<BookmarkEntity> getBookmarkByIdUserAndIdCafe(String idUser, String idCafe) {
        return bookmarkDAO.getBookmarkByIdUserAndIdCafe(idUser,idCafe);
    }
}
