package space.app.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import space.app.DAO.PostDAO;
import space.app.Database.Entity.PostEntity;

@Database(entities = {PostEntity.class}, version = 1)
public abstract class PostDatabase extends RoomDatabase {

    private static PostDatabase instance;
    public abstract PostDAO postDAO();

    public static synchronized PostDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            PostDatabase.class, "Post_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
