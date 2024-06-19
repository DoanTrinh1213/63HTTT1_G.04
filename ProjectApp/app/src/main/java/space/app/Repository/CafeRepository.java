package space.app.Repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import space.app.DAO.CafeDAO;
import space.app.Database.DatabaseRoom;
import space.app.Database.Entity.CafeEntity;

public class CafeRepository {
    private CafeDAO cafeDAO;
    private LiveData<List<CafeEntity>> allCafeRepo;
    private ExecutorService databaseWriteExecutor;
    public CafeRepository(Application application) {
        DatabaseRoom database = DatabaseRoom.getInstance(application);
        cafeDAO = database.cafeDAO();
        allCafeRepo = cafeDAO.getAllCafe();
        databaseWriteExecutor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<CafeEntity>> getAllCafe() {
        return allCafeRepo;
    }
    public void insertCafe(CafeEntity cafeEntity) {
        databaseWriteExecutor.execute(() -> {
            cafeDAO.insertCafe(cafeEntity);
        });
    }

    public void deleteAllCafe() {
        databaseWriteExecutor.execute(() -> {
            cafeDAO.deleteAll();
        });
    }
    public LiveData<List<CafeEntity>> getCafesBySearchTerm(String searchTerm) {
        return cafeDAO.getCafesBySearchTerm(searchTerm);
    }


    public LiveData<List<CafeEntity>> getCafesPostedByUser(String idUser,String idCafe) {
        return cafeDAO.getCafesPostedByUser(idUser,idCafe);
    }

    public LiveData<List<CafeEntity>> getCafeByTopEvaluate() {
        return cafeDAO.getCafeByTopEvaluate();
    }

    public LiveData<List<CafeEntity>> getCafesByFindCoffee() {
        return cafeDAO.getCafesByFindCoffee();
    }

    public LiveData<List<CafeEntity>> getCafesBySearchTermAndFindCoffee(String searchTerm) {
        return cafeDAO.getCafesBySearchTermAndFindCoffee(searchTerm);
    }

    public LiveData<List<CafeEntity>> getCafeByResName(String resname) {
        return cafeDAO.getCafeByResName(resname);
    }

    public LiveData<List<CafeEntity>> getCafeByOpen() {
        return cafeDAO.getCafeByOpen();
    }

    public LiveData<List<CafeEntity>> getCafeByPurpose(List<String> purpose) {
        return cafeDAO.getCafeByPurpose(purpose);
    }

    public LiveData<List<CafeEntity>> getCafeByDistance(String distance) {
        return cafeDAO.getCafeByDistance(distance);
    }

    public LiveData<List<CafeEntity>> getCafeByPrice(String price) {
        return cafeDAO.getCafeByPrice(price);
    }
}
