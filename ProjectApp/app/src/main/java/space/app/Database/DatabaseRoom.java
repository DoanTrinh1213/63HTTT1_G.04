package space.app.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import space.app.DAO.BookmarkDAO;
import space.app.DAO.CafeDAO;
import space.app.DAO.PostDAO;
import space.app.DAO.SearchResultDAO;
import space.app.DAO.UserDAO;
import space.app.Database.Entity.BookmarkEntity;
import space.app.Database.Entity.CafeEntity;
import space.app.Database.Entity.PostEntity;
import space.app.Database.Entity.SearchResultEntity;
import space.app.Database.Entity.UserEntity;

@Database(entities = {CafeEntity.class, PostEntity.class,SearchResultEntity.class, UserEntity.class, BookmarkEntity.class}, version = 6)
public abstract class DatabaseRoom extends androidx.room.RoomDatabase {
    private static DatabaseRoom instance;
    public abstract CafeDAO cafeDAO();
    public abstract PostDAO postDao();
    public abstract UserDAO userDAO();
    public abstract BookmarkDAO bookmarkDAO();
    public abstract SearchResultDAO searchResultDao();


    public static synchronized DatabaseRoom getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseRoom.class, "Cafe_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
