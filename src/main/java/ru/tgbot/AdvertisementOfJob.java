package ru.tgbot;

public class AdvertisementOfJob extends Advertisement{
    public AdvertisementOfJob(String category, String title, String description, int price, String photo) {
        super(category, title, description, price, photo);
    }
    public void setCategory(String транспорт) {
        super.setCategory("Работа");
    }
}
