package ru.tgbot;

public class AdvertisementOfAnimals extends Advertisement{
    public AdvertisementOfAnimals(String category, String title, String description, int price, String photo) {
        super(category, title, description, price, photo);
    }
    public void setCategory(String транспорт) {
        super.setCategory("Животные");
    }
}
