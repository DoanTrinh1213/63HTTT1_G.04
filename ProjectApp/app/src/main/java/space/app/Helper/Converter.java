package space.app.Helper;

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
}
