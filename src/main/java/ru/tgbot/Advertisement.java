package ru.tgbot;

public class Advertisement {
    private String category;
    private String title;
    private String description;
    private int price;
    private String photo;

    public Advertisement(String category, String title, String description, int price, String photo){
        this.category = category;
        this.title = title;
        this.description = description;
        this.price = price;
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
