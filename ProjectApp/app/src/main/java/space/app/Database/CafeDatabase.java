package space.app.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import space.app.DAO.CafeDAO;
import space.app.DAO.SearchResultDAO;
import space.app.Database.Entity.CafeEntity;
import space.app.Database.Entity.SearchResultEntity;

@Database(entities = {CafeEntity.class}, version = 1)
public abstract class CafeDatabase extends RoomDatabase {
    private static CafeDatabase instance;
    public abstract CafeDAO cafeDAO();
    public static synchronized CafeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            CafeDatabase.class, "Cafe_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}