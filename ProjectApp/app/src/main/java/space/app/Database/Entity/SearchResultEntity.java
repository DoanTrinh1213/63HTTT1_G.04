package space.app.Database.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class SearchResultEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String searchQuery;
}
