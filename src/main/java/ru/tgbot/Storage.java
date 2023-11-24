package ru.tgbot;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Storage {
    private Map<Advertisement, String> storage;

    public Storage() {
        this.storage = new HashMap<>();
    }

    public void addAdvertisement(String profileLink, Advertisement ad) {
        storage.put(ad, profileLink);
    }

    public Set<Map.Entry<Advertisement, String>> entrySet() {
        return storage.entrySet();
    }
}
