package ru.tgbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class DemoBot extends TelegramLongPollingBot {
    private final String BOT_TOKEN;
    private final String BOT_NAME;
    private Map<Long, Advertisement> adData = new ConcurrentHashMap<>();
    Storage storage = new Storage();

    public DemoBot(String token, String chatId) {
        this.BOT_TOKEN = token;
        this.BOT_NAME = chatId;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }


    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        String username = update.getMessage().getFrom().getUserName();
        String profileLink = "https://t.me/" + username;
        if (update.hasMessage() && (update.getMessage().hasText() | update.getMessage().hasPhoto())) {
            String messageText = update.getMessage().getText();
            Message message = update.getMessage();
            if ("/start".equals(messageText)) {
                sendProfileInfo(chatId, username, profileLink);
            }
            if ("/create".equals(messageText)) {
                sendOut(chatId, "Введите категорию объявления:");
                adData.put(chatId, new Advertisement("", "", "", 0, ""));
            } else {
                if (adData.containsKey(chatId)) {
                    Advertisement currentAd = adData.get(chatId);
                    if (currentAd.getCategory().isEmpty()) {
                        currentAd.setCategory(messageText);
                        sendOut(chatId, "Введите заголовок объявления:");
                    } else if (currentAd.getTitle().isEmpty()) {
                        currentAd.setTitle(messageText);
                        sendOut(chatId, "Введите описание объявления:");
                    } else if (currentAd.getDescription().isEmpty()) {
                        currentAd.setDescription(messageText);
                        sendOut(chatId, "Введите цену объявления в рублях:");
                    } else if (currentAd.getPrice() == 0) {
                        int price = Integer.parseInt(messageText);
                        currentAd.setPrice(price);
                        sendOut(chatId, "Прикрипите фото ");
                    } else if (currentAd.getPhoto().isEmpty()) {
                        List<PhotoSize> photos = message.getPhoto();
                        String fileId = photos.stream()
                                .sorted((p1, p2) -> Integer.compare(p2.getFileSize(), p1.getFileSize()))
                                .findFirst()
                                .orElse(null)
                                .getFileId();
                        currentAd.setPhoto(fileId);
                        storage.addAdvertisement(profileLink, currentAd);
                        adData.remove(chatId);
                        sendOut(chatId, "Объявление успешно создано!");
                    }
                }
            }
            if ("/view".equals(messageText)) {
                for (Map.Entry<Advertisement, String> entry : storage.entrySet()) {
                    String prLinks = entry.getValue();
                    Advertisement ad = entry.getKey();
                    String adInfo = "Пользователь: " + prLinks + "\n" + "Категория: " + ad.getCategory() + "\n" + "Заголовок: " + ad.getTitle() + "\n" +
                            "Описание: " + ad.getDescription() + "\n" + "Цена (руб): " + ad.getPrice() ;
                    sendPhoto(chatId, ad.getPhoto(), adInfo);
                }
            }
        }
    }


    private void sendPhoto(long chatId, String fileId, String caption){
        SendPhoto response = new SendPhoto();
                    response.setChatId(String.valueOf(chatId));
                    response.setPhoto(new InputFile(fileId));
                    response.setCaption(caption);
            try {
                execute(response);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    private void sendOut(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendProfileInfo(long chatId, String username, String profileLink) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Привет " + username + "! Проверте, указан ли ваш nickname в настройках Telegram (ваше имя не должно быть null). " + "\n" + "Ссылка на ваш профиль: " + profileLink);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}