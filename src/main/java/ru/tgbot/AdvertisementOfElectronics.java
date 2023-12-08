package ru.tgbot;

public class AdvertisementOfElectronics extends Advertisement{
    public AdvertisementOfElectronics(String category, String title, String description, int price, String photo) {
        super(category, title, description, price, photo);
    }
    public void setCategory(String транспорт) {
        super.setCategory("Электроника");
    }
}
