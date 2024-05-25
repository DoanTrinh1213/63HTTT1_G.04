package space.app.Model;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String userName;
    private String email;
    private  String image;
    private String describe;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public User(String id, String userName, String email, String image, String describe){
        this.id = id;
        this.userName = userName;
        this.email= email;
        this.image= image;
        this.describe= describe;
    }
    public User(String id,String email){
        this.id=id;
        this.userName=id;
        this.email = email;
        this.image="";
        this.describe = "";
    }
}
