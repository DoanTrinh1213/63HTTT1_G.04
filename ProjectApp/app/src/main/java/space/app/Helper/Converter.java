package space.app.Helper;

import java.util.ArrayList;
import java.util.List;

import space.app.Database.Entity.CafeEntity;
import space.app.Model.Cafe;

public class Converter {
    public static Cafe convertCafeEntityToCafeModel(CafeEntity cafeEntity) {
        Cafe cafe = new Cafe();
        cafe.setIdCafe(cafeEntity.getIdCafe());
        cafe.setAddress(cafeEntity.getAddress());
        cafe.setResName(cafeEntity.getResName());
        cafe.setContact(cafeEntity.getContact());
        cafe.setDescribe(cafeEntity.getDescribe());
        cafe.setEvaluate(cafeEntity.getEvaluate());
        cafe.setLink(cafeEntity.getLink());
        cafe.setImages(cafeEntity.getImages());
        cafe.setPrice(cafeEntity.getPrice());
        cafe.setPurpose(cafeEntity.getPurpose());
        cafe.setTimeOpen(cafeEntity.getTimeOpen());
        cafe.setMenu(cafeEntity.getMenu());
        cafe.setIdUser(cafeEntity.getIdUser());
        return cafe;
    }

    public List<String> reverseListString(List<String> list){
        List<String> reverseList =  new ArrayList<>();
        for(int i = list.size(); i>=0; i--){
            reverseList.add(list.get(i));
        }
        return reverseList;
    };
}
