package space.app.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import space.app.DAO.SearchResultDAO;
import space.app.Database.Entity.SearchResultEntity;

@Database(entities = {SearchResultEntity.class}, version = 1)
public abstract class SearchDatabase extends RoomDatabase {
    private static SearchDatabase instance;
    public abstract SearchResultDAO searchResultDao();
    public static synchronized SearchDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            SearchDatabase.class, "search_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
