package space.app.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import space.app.Database.Entity.CafeEntity;
import space.app.Repository.CafeRepository;

public class CafeViewModel extends AndroidViewModel {
    private CafeRepository cafeRepository;

    public CafeViewModel(Application application) {
        super(application);
        cafeRepository = new CafeRepository(application);
    }

    public LiveData<List<CafeEntity>> getCafesBySearchTerm(String searchTerm) {
        return cafeRepository.getCafesBySearchTerm(searchTerm);
    }

    public LiveData<List<CafeEntity>> getAllCafe(){
        return cafeRepository.getAllCafe();
    }
}
