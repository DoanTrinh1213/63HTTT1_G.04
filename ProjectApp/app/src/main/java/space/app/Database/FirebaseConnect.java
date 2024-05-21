package space.app.Database;

import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConnect {
    private static FirebaseDatabase firebaseDatabase;
    public FirebaseConnect(){
        firebaseDatabase = FirebaseDatabase.getInstance();
    }
    public boolean addChild(String parentNode,String childNode,Object data){
        try{
            firebaseDatabase.getReference(parentNode).child(childNode).setValue(data);
        }catch (Exception e){
            Log.e("Error","Check database");
            return false;
        }
        return true;
    }
}
