package space.app.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import space.app.Database.Entity.BookmarkEntity;
import space.app.Repository.BookmarkRepository;

public class BookmarkViewModel extends AndroidViewModel {
    private BookmarkRepository bookmarkRepository;
    public BookmarkViewModel(Application application){
        super(application);
        bookmarkRepository = new BookmarkRepository(application);
    }
    public void insertBookmark(BookmarkEntity entity){
        bookmarkRepository.insertBookmark(entity);
    }
    public void deleteBookmark(BookmarkEntity entity){
        bookmarkRepository.deleteBookmark(entity);
    }

    public LiveData<List<BookmarkEntity>> getAllBookmarkByIdUser(String idUser){
        return bookmarkRepository.getAllBookmarkByIduser(idUser);
    }

    public LiveData<BookmarkEntity> getBookmarkByIdUserAndIdCafe(String idUser,String idCafe){
        return bookmarkRepository.getBookmarkByIdUserAndIdCafe(idUser,idCafe);
    }
}
