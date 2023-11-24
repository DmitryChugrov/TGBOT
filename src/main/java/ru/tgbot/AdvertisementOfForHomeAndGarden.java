package ru.tgbot;

public class AdvertisementOfForHomeAndGarden extends Advertisement{
    public AdvertisementOfForHomeAndGarden(String category, String title, String description, int price, String photo) {
        super(category, title, description, price, photo);
    }
    public void setCategory() {
        super.setCategory("Дом и сад");
    }
}
