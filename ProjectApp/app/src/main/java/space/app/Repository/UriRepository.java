package space.app.Repository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import space.app.DAO.UriDAO;
import space.app.Database.DatabaseRoom;
import space.app.Database.Entity.UriEntity;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Entity;

public class UriRepository {
    private UriDAO uriDAO;
    private ExecutorService executorService;

    public UriRepository(Application application){
        DatabaseRoom databaseRoom = DatabaseRoom.getInstance(application);
        uriDAO = databaseRoom.uriDAO();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertUri(UriEntity uri){
        executorService.execute(()->{
            uriDAO.insertUri(uri);
        });
    }
    public void deleteUri(UriEntity uri){
        executorService.execute(()->{
            uriDAO.deleteUri(uri);
        });
    }
    public void deleteAllUri(){
        executorService.execute(()->{
            uriDAO.deleteAllUri();
        });
    }

    public LiveData<List<UriEntity>> getUriByIdCafeAndType(String idCafe ,String type){
        return uriDAO.getUriByIdCafeAndType(idCafe,type);
    }
}
