package space.app.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import space.app.DAO.CafeDAO;
import space.app.Database.Entity.CafeEntity;
import space.app.Model.Cafe;
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
    public LiveData<List<CafeEntity>> getCafesBySearchTermAndFindCoffee(String searchTerm) {
        return cafeRepository.getCafesBySearchTermAndFindCoffee(searchTerm);
    }

    public LiveData<List<CafeEntity>> getAllCafe(){
        return cafeRepository.getAllCafe();
    }

    public LiveData<List<CafeEntity>> getCafesPostedByUser(String idUser,String idCafe){
        return cafeRepository.getCafesPostedByUser(idUser,idCafe);
    }

    public LiveData<List<CafeEntity>> getCafesByTopEvaluate(){
        return cafeRepository.getCafeByTopEvaluate();
    }

    public LiveData<List<CafeEntity>> getCafesByFindCoffee(){
        return cafeRepository.getCafesByFindCoffee();
    }

    public void setCafeList(List<Cafe> cafeList) {
        for(Cafe cafe:cafeList) {
            CafeEntity cafeEntity = new CafeEntity();
            cafeEntity.setIdCafe(cafe.getIdCafe());
            cafeEntity.setAddress(cafe.getAddress());
            cafeEntity.setLink(cafe.getLink());
            cafeEntity.setMenu(cafe.getMenu());
            cafeEntity.setDescribe(cafe.getDescribe());
            cafeEntity.setPrice(cafe.getPrice());
            cafeEntity.setContact(cafe.getContact());
            cafeEntity.setPurpose(cafe.getPurpose());
            cafeEntity.setEvaluate(cafe.getEvaluate());
            cafeEntity.setResName(cafe.getResName());
            cafeEntity.setTimeOpen(cafe.getTimeOpen());
            cafeEntity.setImages(cafe.getImages());
            cafeEntity.setIdUser(cafe.getIdUser());
            cafeRepository.insertCafe(cafeEntity);
        }
    }

    public LiveData<List<CafeEntity>> getCafeByTopEvaluate() {
        return cafeRepository.getCafeByTopEvaluate();
    }
}
