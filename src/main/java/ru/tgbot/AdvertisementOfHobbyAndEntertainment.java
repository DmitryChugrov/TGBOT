package ru.tgbot;

public class AdvertisementOfHobbyAndEntertainment extends Advertisement{
    public AdvertisementOfHobbyAndEntertainment(String category, String title, String description, int price, String photo) {
        super(category, title, description, price, photo);
    }
    public void setCategory(String транспорт) {
        super.setCategory("Хобби и отдых");
    }
}
