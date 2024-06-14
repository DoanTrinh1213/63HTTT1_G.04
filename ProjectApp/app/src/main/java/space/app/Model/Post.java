package space.app.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Post implements Serializable {
    private String id;
    private String idCafe;
    private String idUser;
    private String comment;
    private String star;
    private ArrayList<String> imagesCommentsPath;

    // Getters
    public String getId() {
        return id;
    }

    public String getIdCafe() {
        return idCafe;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getComment() {
        return comment;
    }

    public ArrayList<String> getImagesCommentsPath() {
        return imagesCommentsPath;
    }

    public String getStar() {
        return star;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setIdCafe(String idRes) {
        this.idCafe = idRes;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setImagesCommentsPath(ArrayList<String> imagesCommentsPath) {
        this.imagesCommentsPath = imagesCommentsPath;
    }

    public void setStar(String star) {
        this.star = star;
    }

    // Constructor
    public Post(String id, String idCafe, String idUser, String comment, ArrayList<String> imagesCommentsPath, String star) {
        this.id = id;
        this.idCafe = idCafe;
        this.idUser = idUser;
        this.comment = comment;
        this.imagesCommentsPath = imagesCommentsPath;
        this.star = star;
    }

    public Post() {
    }
}