package ru.tgbot;

public class AdvertisementOfProperty extends Advertisement {

    public AdvertisementOfProperty(String category, String title, String description, int price, String photo) {
        super(category, title, description, price, photo);
    }
    public void setCategory(String транспорт) {
        super.setCategory("Недвижимость");
    }
}
