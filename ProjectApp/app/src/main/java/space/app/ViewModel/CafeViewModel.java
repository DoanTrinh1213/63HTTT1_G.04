package space.app.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public LiveData<List<CafeEntity>> getAllCafe() {
        return cafeRepository.getAllCafe();
    }

    public LiveData<List<CafeEntity>> getCafesPostedByUser(String idUser, String idCafe) {
        return cafeRepository.getCafesPostedByUser(idUser, idCafe);
    }

    public LiveData<List<CafeEntity>> getCafesByTopEvaluate() {
        return cafeRepository.getCafeByTopEvaluate();
    }

    public LiveData<List<CafeEntity>> getCafesByFindCoffee() {
        return cafeRepository.getCafesByFindCoffee();
    }

    public void setCafeList(List<Cafe> cafeList) {
        for (Cafe cafe : cafeList) {
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

    public LiveData<List<CafeEntity>> getCafeByResName(String resname) {
        return cafeRepository.getCafeByResName(resname);
    }

    public LiveData<List<CafeEntity>> getCafeByOpen() {
        return cafeRepository.getCafeByOpen();
    }

    public LiveData<List<CafeEntity>> getCafeByPurpose(List<String> purpose) {
        return cafeRepository.getCafeByPurpose(purpose);
    }

    public LiveData<List<CafeEntity>> getCafeByDistance(String distance) {
        return cafeRepository.getCafeByDistance(distance);
    }

    public LiveData<List<CafeEntity>> getCafeByPrice(String price) {
        return cafeRepository.getCafeByPrice(price);
    }

    public LiveData<List<CafeEntity>> getCafesByCriteria(String time, List<String> purposes, String distance, String price) {
        MediatorLiveData<List<CafeEntity>> mergedLiveData = new MediatorLiveData<>();
        List<CafeEntity> mergedList = new ArrayList<>();
        if (time != null) {
            LiveData<List<CafeEntity>> cafesByOpen = cafeRepository.getCafeByOpen();
            mergedLiveData.addSource(cafesByOpen, cafes -> {
                if (!cafes.isEmpty()) {
                    Log.d("Cafe1",String.valueOf(cafes.size()));
                    if(mergedList.isEmpty()){
                        mergedList.addAll(cafes);
                    }
                    else{
                        mergedList.retainAll(cafes);
                    }
                    mergedLiveData.setValue(mergedList);
                }
            });
        }
        if (!purposes.isEmpty()) {
            LiveData<List<CafeEntity>> cafesByPurpose = cafeRepository.getCafeByPurpose(purposes);
            mergedLiveData.addSource(cafesByPurpose, cafes -> {
                if (!cafes.isEmpty()) {
                    Log.d("Cafe2",String.valueOf(cafes.size()));

                    if(mergedList.isEmpty()){
                        mergedList.addAll(cafes);
                    }
                    else{
                        mergedList.retainAll(cafes);
                    }
                    mergedLiveData.setValue(mergedList);
                }
            });
        }
        if (distance != null) {
            LiveData<List<CafeEntity>> cafesByDistance = cafeRepository.getCafeByDistance(distance);
            mergedLiveData.addSource(cafesByDistance, cafes -> {
                if (!cafes.isEmpty()) {
                    Log.d("Cafe3",String.valueOf(cafes.size()));

                    if(mergedList.isEmpty()){
                        mergedList.addAll(cafes);
                    }
                    else{
                        mergedList.retainAll(cafes);
                    }
                    mergedLiveData.setValue(mergedList);
                }
            });
        }
        if (price != null) {
            LiveData<List<CafeEntity>> cafesByPrice = cafeRepository.getCafeByPrice(price);
            mergedLiveData.addSource(cafesByPrice, cafes -> {
                if (!cafes.isEmpty()) {
                    Log.d("Cafe4",String.valueOf(cafes.size()));

                    if(mergedList.isEmpty()){
                        mergedList.addAll(cafes);
                    }
                    else{
                        mergedList.retainAll(cafes);
                    }
                    mergedLiveData.setValue(mergedList);
                }
            });
        }

        return mergedLiveData;
    }

    public LiveData<List<CafeEntity>> getCafesOrderBy(List<String> orders, String time, List<String> purposes, String distance, String price) {
        LiveData<List<CafeEntity>> cafesByCriteria = getCafesByCriteria(time, purposes, distance, price); // Thay thế tham số null bằng các tham số thích hợp

        MediatorLiveData<List<CafeEntity>> orderedLiveData = new MediatorLiveData<>();
        orderedLiveData.addSource(cafesByCriteria, cafes -> {
            List<CafeEntity> orderedCafes = new ArrayList<>(cafes);

            if (orders != null && !orders.isEmpty()) {
                for (String order : orders) {
                    switch (order) {
                        case "distance":
                            sortCafesByDistance(orderedCafes);
                            break;
                        case "cheap_price":
                            sortCafesByCheapPrice(orderedCafes);
                            break;
                        case "expensive_price":
                            sortCafesByExpensivePrice(orderedCafes);
                        default:
                            break;
                    }
                }
            }

            orderedLiveData.setValue(orderedCafes);
        });

        return orderedLiveData;
    }

    public LiveData<CafeEntity> getCafeByIdCafe(String idCafe){
        return cafeRepository.getCafeByIdCafe(idCafe);
    };

    public LiveData<List<CafeEntity>> getAllCafeByBookmark(String idUser){
        return cafeRepository.getAllCafeByBookmark(idUser);
    }

    private void sortCafesByDistance(List<CafeEntity> cafes) {
        Collections.sort(cafes, new Comparator<CafeEntity>() {
            @Override
            public int compare(CafeEntity cafe1, CafeEntity cafe2) {
                return Double.compare(0, 1);
            }
        });
    }

    private void sortCafesByCheapPrice(List<CafeEntity> cafes) {
        Collections.sort(cafes, new Comparator<CafeEntity>() {
            @Override
            public int compare(CafeEntity cafe1, CafeEntity cafe2) {
                return Double.compare(cafe1.getPrice(), cafe2.getPrice());
            }
        });
    }

    private void sortCafesByExpensivePrice(List<CafeEntity> cafes) {
        Collections.sort(cafes, new Comparator<CafeEntity>() {
            @Override
            public int compare(CafeEntity cafe1, CafeEntity cafe2) {
                return Double.compare(cafe2.getPrice(), cafe1.getPrice());
            }
        });
    }
}
