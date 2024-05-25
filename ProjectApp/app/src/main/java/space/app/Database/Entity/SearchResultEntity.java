package space.app.Database.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "searchResult")
public class SearchResutlEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String searchQuery;
}
