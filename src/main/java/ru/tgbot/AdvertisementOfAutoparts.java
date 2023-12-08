package ru.tgbot;

public class AdvertisementOfAutoparts extends Advertisement{
    public AdvertisementOfAutoparts(String category, String title, String description, int price, String photo) {
        super(category, title, description, price, photo);
    }
    public void setCategory(String транспорт) {
        super.setCategory("Автозапчасти");
    }
}
