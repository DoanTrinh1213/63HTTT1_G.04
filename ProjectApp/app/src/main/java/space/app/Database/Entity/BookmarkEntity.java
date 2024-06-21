package space.app.Database.Entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmarks")
public class BookmarkEntity {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdCafe() {
        return idCafe;
    }

    public void setIdCafe(String idCafe) {
        this.idCafe = idCafe;
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    int id;
    String idUser;
    String idCafe;
}
