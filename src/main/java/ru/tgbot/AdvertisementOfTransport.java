package ru.tgbot;

public class AdvertisementOfTransport extends Advertisement{
    public AdvertisementOfTransport(String category, String title, String description, int price, String photo) {
        super(category, title, description, price, photo);
    }
    public void setCategory() {
        super.setCategory("Транспорт");
    }
}
