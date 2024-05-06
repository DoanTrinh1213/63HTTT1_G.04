package space.app.object;

public class Post {
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
    public String getImagesCommentsPath(){
        return imagesCommentsPath;
    }
    public String getStar(){
        return star;
    }

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
    public void setImagesCommentsPath(String imagesCommentsPath){
        this.imagesCommentsPath = imagesCommentsPath;
    }
    public void setStar(String star){
        this.star = star;
    }
    public Post(String id, String idCafe, String idUser, String comment,String imagesCommentsPath,String star) {
        this.id = id;
        this.idCafe = idCafe;
        this.idUser = idUser;
        this.comment = comment;
        this.imagesCommentsPath = imagesCommentsPath;
        this.star = star;
    }

    private String id;
    private String idCafe;
    private String idUser;
    private String comment;
    private String star;
    private String imagesCommentsPath;
}
