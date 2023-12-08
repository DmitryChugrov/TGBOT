package ru.tgbot;

public class AdvertisementOfServices extends Advertisement{
    public AdvertisementOfServices(String category, String title, String description, int price, String photo) {
        super(category, title, description, price, photo);
    }
    public void setCategory(String транспорт) {
        super.setCategory("Услуги");
    }
}
