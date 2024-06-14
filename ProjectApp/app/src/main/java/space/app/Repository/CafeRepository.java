package space.app.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import space.app.DAO.CafeDAO;
import space.app.Database.RoomDatabase;
import space.app.Database.Entity.CafeEntity;
import space.app.Model.Cafe;

public class CafeRepository {
    private CafeDAO cafeDAO;
    private LiveData<List<CafeEntity>> allCafeRepo;
    private ExecutorService databaseWriteExecutor;
    public CafeRepository(Application application) {
        RoomDatabase database = RoomDatabase.getInstance(application);
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

    public LiveData<List<CafeEntity>> helloWorld(){
        return cafeDAO.getAllCafe1();
    }
}
