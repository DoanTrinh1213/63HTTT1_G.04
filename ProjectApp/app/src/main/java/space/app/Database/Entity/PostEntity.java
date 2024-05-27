package space.app.Database.Entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "post")
public class PostEntity {
    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getIdCafe() {
        return idCafe;
    }

    public void setIdCafe(String idCafe) {
        this.idCafe = idCafe;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getImagesCommentsPath() {
        return imagesCommentsPath;
    }

    public void setImagesCommentsPath(String imagesCommentsPath) {
        this.imagesCommentsPath = imagesCommentsPath;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String id;
    private String idCafe;
    private String idUser;
    private String imagesCommentsPath;
    private String star;
    private String comment;
}
