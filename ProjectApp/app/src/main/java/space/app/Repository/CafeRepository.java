package space.app.Repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import space.app.DAO.CafeDAO;
import space.app.Database.CafeDatabase;
import space.app.Database.Entity.CafeEntity;
import space.app.Database.Entity.SearchResultEntity;
import space.app.Database.SearchDatabase;
import space.app.Model.Cafe;

public class CafeRepository {
    private CafeDAO cafeDAO;
    private LiveData<List<CafeEntity>> allCafeRepo;
    private ExecutorService databaseWriteExecutor;
    public CafeRepository(Application application) {
        CafeDatabase database = CafeDatabase.getInstance(application);
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



}
