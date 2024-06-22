package space.app.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import space.app.Database.Entity.UriEntity;
import space.app.Repository.UriRepository;

public class UriViewModel extends AndroidViewModel {
    private UriRepository uriRepository;
    public UriViewModel(@NonNull Application application) {
        super(application);
        uriRepository = new UriRepository(application);
    }

    public void insertUri(UriEntity uri){
        uriRepository.insertUri(uri);
    }
    public void deleteAllUri(){
        uriRepository.deleteAllUri();
    }
    public LiveData<List<UriEntity>> getUriByIdCafeAndType(String idCafe,String type){
        return uriRepository.getUriByIdCafeAndType(idCafe,type);
    }
}
