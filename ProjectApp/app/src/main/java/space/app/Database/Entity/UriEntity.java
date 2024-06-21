package space.app.Database.Entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "uri")
public class UriEntity {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdCafe() {
        return idCafe;
    }

    public void setIdCafe(String idCafe) {
        this.idCafe = idCafe;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    int id;
    String idCafe;
    String uri;
    String type;
}
