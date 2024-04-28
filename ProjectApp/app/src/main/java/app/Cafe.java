package app;

public class Cafe {
    private String resName;
    private String address;
    private String describe;
    private Float price;
    private String menu;
    private String timeOpen;
    private String contact;
    private String images;
    private String evaluate;
    private String link;

    public Cafe(String resName, String address, String describe, Float price, String menu, String timeOpen, String contact, String images, String evaluate, String link) {
        this.resName = resName;
        this.address = address;
        this.describe = describe;
        this.price = price;
        this.menu = menu;
        this.timeOpen = timeOpen;
        this.contact = contact;
        this.images = images;
        this.evaluate = evaluate;
        this.link = link;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getTimeOpen() {
        return timeOpen;
    }

    public void setTimeOpen(String timeOpen) {
        this.timeOpen = timeOpen;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}