package space.app.Helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import space.app.Model.Post;

public class PostHelper {

    private static PostHelper instance;
    private List<Post> posts;
    private PostHelper(){
        this.posts = new ArrayList<>();
    }
    public static synchronized PostHelper getInstance(){
        if(instance==null){
            instance = new PostHelper();
        }
        return instance;
    }

    public void addPost(Post post){
        this.posts.add(post);
    }

    public void deleteAllPost(){
        this.posts.clear();
    }

    public List<Post> getPostLimit5StarHigh(List<Post> posts){
        List<Post> postShort = posts;
        List<Post> result = new ArrayList<>();
        Collections.sort(postShort, new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                return o2.getStar().compareTo(o1.getStar());
            }
        });
        if(posts.size()>=5){
            for(int i =0 ;i<5;i++){
                result.add(postShort.get(i));
            }
        }
        else{
            for(int i =0;i<posts.size();i++){
                result.add(postShort.get(i));
            }
        }
        return result;
    }

    public List<Post> getAllPostByIdCafe(String idCafe){
        List<Post> postShort = this.posts;
        Collections.sort(postShort, new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                return o2.getTimestamp().compareTo(o1.getTimestamp());
            }
        });
        List<Post> postShop = new ArrayList<>();
        for(Post post : postShort){
            if(post.getIdCafe().equals(idCafe)){
                postShop.add(post);
            }
        }
        return postShop;
    };
    public Post getPostByIdAndUser(String idCafe,String idUsername){
        List<Post> posts=  getAllPostByIdCafe(idCafe);
        Post user = new Post();
        for(Post post : posts){
            if(post.getIdUser().equals(idUsername)){
                user = post;
                break;
            }
        }
        return user;
    }

    public Float starRating(String idCafe){
        List<Post> posts = getAllPostByIdCafe(idCafe);
        Float sum= 0.0F;
        for(Post post:posts){
            sum += Float.parseFloat(post.getStar());
        }
        return sum/posts.size();
    }
}
