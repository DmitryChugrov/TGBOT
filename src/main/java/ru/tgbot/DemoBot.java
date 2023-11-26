package ru.tgbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
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
            if ("/createtransport".equals(messageText)) {
                adData.put(chatId, new AdvertisementOfTransport("", "", "", 0, ""));
                sendOut(chatId, "Введите заголовок объявления");
            }else if ("/createproperty".equals(messageText)) {
                    adData.put(chatId, new AdvertisementOfProperty("", "", "", 0, ""));
                    sendOut(chatId, "Введите заголовок объявления");
            }
            else if ("/createservices".equals(messageText)) {
                adData.put(chatId, new AdvertisementOfServices("", "", "", 0, ""));
                sendOut(chatId, "Введите заголовок объявления");
            }
            else if ("/createjob".equals(messageText)) {
                adData.put(chatId, new AdvertisementOfJob("", "", "", 0, ""));
                sendOut(chatId, "Введите заголовок объявления");
            }
            else if ("/createhobbyandentertainment".equals(messageText)) {
                adData.put(chatId, new AdvertisementOfHobbyAndEntertainment("", "", "", 0, ""));
                sendOut(chatId, "Введите заголовок объявления");
            }
            else if ("/createforhomeandgarden".equals(messageText)) {
                adData.put(chatId, new AdvertisementOfForHomeAndGarden("", "", "", 0, ""));
                sendOut(chatId, "Введите заголовок объявления");
            }
            else if ("/createelectronics".equals(messageText)) {
                adData.put(chatId, new AdvertisementOfElectronics("", "", "", 0, ""));
                sendOut(chatId, "Введите заголовок объявления");
            }
            else if ("/createautoparts".equals(messageText)) {
                adData.put(chatId, new AdvertisementOfAutoparts("", "", "", 0, ""));
                sendOut(chatId, "Введите заголовок объявления");
            }
            else if ("/createanimals".equals(messageText)) {
                adData.put(chatId, new AdvertisementOfAnimals("", "", "", 0, ""));
                sendOut(chatId, "Введите заголовок объявления");
            }
            else {
                    if (adData.containsKey(chatId)) {
                        Advertisement currentAd = adData.get(chatId);
                        if (currentAd instanceof AdvertisementOfTransport) {
                            AdvertisementOfTransport currentTransportAd = (AdvertisementOfTransport) currentAd;
                            currentTransportAd.setCategory();
                            if (currentTransportAd.getTitle().isEmpty()) {
                                currentTransportAd.setTitle(messageText);
                                sendOut(chatId, "Введите описание объявления");
                            } else if (currentTransportAd.getDescription().isEmpty()) {
                                currentTransportAd.setDescription(messageText);
                                sendOut(chatId, "Введите цену объявления в рублях");
                            } else if (currentTransportAd.getPrice() == 0) {
                                int price = Integer.parseInt(messageText);
                                currentTransportAd.setPrice(price);
                                sendOut(chatId, "Прикрипите фото");
                            } else if (currentTransportAd.getPhoto().isEmpty()) {
                                List<PhotoSize> photos = message.getPhoto();
                                String fileId = photos.stream()
                                        .sorted((p1, p2) -> Integer.compare(p2.getFileSize(), p1.getFileSize()))
                                        .findFirst()
                                        .orElse(null)
                                        .getFileId();
                                currentTransportAd.setPhoto(fileId);
                                storage.addAdvertisement(profileLink, currentTransportAd);
                                adData.remove(chatId);
                                sendOut(chatId, "Объявление в категории " + currentTransportAd.getCategory() + " успешно создано!");
                            }
                        } else if (currentAd instanceof AdvertisementOfProperty) {
                            AdvertisementOfProperty currentPropertyAd = (AdvertisementOfProperty) currentAd;
                            currentPropertyAd.setCategory();
                            if (currentPropertyAd.getTitle().isEmpty()) {
                                currentPropertyAd.setTitle(messageText);
                                sendOut(chatId, "Введите описание объявления");
                            } else if (currentPropertyAd.getDescription().isEmpty()) {
                                currentPropertyAd.setDescription(messageText);
                                sendOut(chatId, "Введите цену объявления в рублях");
                            } else if (currentPropertyAd.getPrice() == 0) {
                                int price = Integer.parseInt(messageText);
                                currentPropertyAd.setPrice(price);
                                sendOut(chatId, "Прикрипите фото");
                            } else if (currentPropertyAd.getPhoto().isEmpty()) {
                                List<PhotoSize> photos = message.getPhoto();
                                String fileId = photos.stream()
                                        .sorted((p1, p2) -> Integer.compare(p2
                                                .getFileSize(), p1.getFileSize()))
                                        .findFirst()
                                        .orElse(null)
                                        .getFileId();
                                currentPropertyAd.setPhoto(fileId);
                                storage.addAdvertisement(profileLink, currentPropertyAd);
                                adData.remove(chatId);
                                sendOut(chatId, "Объявление в категории " + currentPropertyAd.getCategory() + " успешно создано!");
                            }
                        }else if (currentAd instanceof AdvertisementOfServices) {
                            AdvertisementOfServices currentServicesAd = (AdvertisementOfServices) currentAd;
                            currentServicesAd.setCategory();
                            if (currentServicesAd.getTitle().isEmpty()) {
                                currentServicesAd.setTitle(messageText);
                                sendOut(chatId, "Введите описание объявления");
                            } else if (currentServicesAd.getDescription().isEmpty()) {
                                currentServicesAd.setDescription(messageText);
                                sendOut(chatId, "Введите цену объявления в рублях");
                            } else if (currentServicesAd.getPrice() == 0) {
                                int price = Integer.parseInt(messageText);
                                currentServicesAd.setPrice(price);
                                sendOut(chatId, "Прикрипите фото");
                            } else if (currentServicesAd.getPhoto().isEmpty()) {
                                List<PhotoSize> photos = message.getPhoto();
                                String fileId = photos.stream()
                                        .sorted((p1, p2) -> Integer.compare(p2
                                                .getFileSize(), p1.getFileSize()))
                                        .findFirst()
                                        .orElse(null)
                                        .getFileId();
                                currentServicesAd.setPhoto(fileId);
                                storage.addAdvertisement(profileLink, currentServicesAd);
                                adData.remove(chatId);
                                sendOut(chatId, "Объявление в категории " + currentServicesAd.getCategory() + " успешно создано!");
                            }
                        }else if (currentAd instanceof AdvertisementOfJob) {
                            AdvertisementOfJob currentJobAd = (AdvertisementOfJob) currentAd;
                            currentJobAd.setCategory();
                            if (currentJobAd.getTitle().isEmpty()) {
                                currentJobAd.setTitle(messageText);
                                sendOut(chatId, "Введите описание объявления");
                            } else if (currentJobAd.getDescription().isEmpty()) {
                                currentJobAd.setDescription(messageText);
                                sendOut(chatId, "Введите цену объявления в рублях");
                            } else if (currentJobAd.getPrice() == 0) {
                                int price = Integer.parseInt(messageText);
                                currentJobAd.setPrice(price);
                                sendOut(chatId, "Прикрипите фото");
                            } else if (currentJobAd.getPhoto().isEmpty()) {
                                List<PhotoSize> photos = message.getPhoto();
                                String fileId = photos.stream()
                                        .sorted((p1, p2) -> Integer.compare(p2
                                                .getFileSize(), p1.getFileSize()))
                                        .findFirst()
                                        .orElse(null)
                                        .getFileId();
                                currentJobAd.setPhoto(fileId);
                                storage.addAdvertisement(profileLink, currentJobAd);
                                adData.remove(chatId);
                                sendOut(chatId, "Объявление в категории " + currentJobAd.getCategory() + " успешно создано!");
                            }
                        }
                        else if (currentAd instanceof AdvertisementOfHobbyAndEntertainment) {
                            AdvertisementOfHobbyAndEntertainment currentHobbyAndEntertainmentAd = (AdvertisementOfHobbyAndEntertainment) currentAd;
                            currentHobbyAndEntertainmentAd.setCategory();
                            if (currentHobbyAndEntertainmentAd.getTitle().isEmpty()) {
                                currentHobbyAndEntertainmentAd.setTitle(messageText);
                                sendOut(chatId, "Введите описание объявления");
                            } else if (currentHobbyAndEntertainmentAd.getDescription().isEmpty()) {
                                currentHobbyAndEntertainmentAd.setDescription(messageText);
                                sendOut(chatId, "Введите цену объявления в рублях");
                            } else if (currentHobbyAndEntertainmentAd.getPrice() == 0) {
                                int price = Integer.parseInt(messageText);
                                currentHobbyAndEntertainmentAd.setPrice(price);
                                sendOut(chatId, "Прикрипите фото");
                            } else if (currentHobbyAndEntertainmentAd.getPhoto().isEmpty()) {
                                List<PhotoSize> photos = message.getPhoto();
                                String fileId = photos.stream()
                                        .sorted((p1, p2) -> Integer.compare(p2
                                                .getFileSize(), p1.getFileSize()))
                                        .findFirst()
                                        .orElse(null)
                                        .getFileId();
                                currentHobbyAndEntertainmentAd.setPhoto(fileId);
                                storage.addAdvertisement(profileLink, currentHobbyAndEntertainmentAd);
                                adData.remove(chatId);
                                sendOut(chatId, "Объявление в категории " + currentHobbyAndEntertainmentAd.getCategory() + " успешно создано!");
                            }
                        }else if (currentAd instanceof AdvertisementOfForHomeAndGarden) {
                            AdvertisementOfForHomeAndGarden currentForHomeAndGardenAd = (AdvertisementOfForHomeAndGarden) currentAd;
                            currentForHomeAndGardenAd.setCategory();
                            if (currentForHomeAndGardenAd.getTitle().isEmpty()) {
                                currentForHomeAndGardenAd.setTitle(messageText);
                                sendOut(chatId, "Введите описание объявления");
                            } else if (currentForHomeAndGardenAd.getDescription().isEmpty()) {
                                currentForHomeAndGardenAd.setDescription(messageText);
                                sendOut(chatId, "Введите цену объявления в рублях");
                            } else if (currentForHomeAndGardenAd.getPrice() == 0) {
                                int price = Integer.parseInt(messageText);
                                currentForHomeAndGardenAd.setPrice(price);
                                sendOut(chatId, "Прикрипите фото");
                            } else if (currentForHomeAndGardenAd.getPhoto().isEmpty()) {
                                List<PhotoSize> photos = message.getPhoto();
                                String fileId = photos.stream()
                                        .sorted((p1, p2) -> Integer.compare(p2
                                                .getFileSize(), p1.getFileSize()))
                                        .findFirst()
                                        .orElse(null)
                                        .getFileId();
                                currentForHomeAndGardenAd.setPhoto(fileId);
                                storage.addAdvertisement(profileLink, currentForHomeAndGardenAd);
                                adData.remove(chatId);
                                sendOut(chatId, "Объявление в категории " + currentForHomeAndGardenAd.getCategory() + " успешно создано!");
                            }
                        }else if (currentAd instanceof AdvertisementOfElectronics) {
                            AdvertisementOfElectronics currentElectronicsAd = (AdvertisementOfElectronics) currentAd;
                            currentElectronicsAd.setCategory();
                            if (currentElectronicsAd.getTitle().isEmpty()) {
                                currentElectronicsAd.setTitle(messageText);
                                sendOut(chatId, "Введите описание объявления");
                            } else if (currentElectronicsAd.getDescription().isEmpty()) {
                                currentElectronicsAd.setDescription(messageText);
                                sendOut(chatId, "Введите цену объявления в рублях");
                            } else if (currentElectronicsAd.getPrice() == 0) {
                                int price = Integer.parseInt(messageText);
                                currentElectronicsAd.setPrice(price);
                                sendOut(chatId, "Прикрипите фото");
                            } else if (currentElectronicsAd.getPhoto().isEmpty()) {
                                List<PhotoSize> photos = message.getPhoto();
                                String fileId = photos.stream()
                                        .sorted((p1, p2) -> Integer.compare(p2
                                                .getFileSize(), p1.getFileSize()))
                                        .findFirst()
                                        .orElse(null)
                                        .getFileId();
                                currentElectronicsAd.setPhoto(fileId);
                                storage.addAdvertisement(profileLink, currentElectronicsAd);
                                adData.remove(chatId);
                                sendOut(chatId, "Объявление в категории " + currentElectronicsAd.getCategory() + " успешно создано!");
                            }
                        }else if (currentAd instanceof AdvertisementOfAutoparts) {
                            AdvertisementOfAutoparts currentAutopartsAd = (AdvertisementOfAutoparts) currentAd;
                            currentAutopartsAd.setCategory();
                            if (currentAutopartsAd.getTitle().isEmpty()) {
                                currentAutopartsAd.setTitle(messageText);
                                sendOut(chatId, "Введите описание объявления");
                            } else if (currentAutopartsAd.getDescription().isEmpty()) {
                                currentAutopartsAd.setDescription(messageText);
                                sendOut(chatId, "Введите цену объявления в рублях");
                            } else if (currentAutopartsAd.getPrice() == 0) {
                                int price = Integer.parseInt(messageText);
                                currentAutopartsAd.setPrice(price);
                                sendOut(chatId, "Прикрипите фото");
                            } else if (currentAutopartsAd.getPhoto().isEmpty()) {
                                List<PhotoSize> photos = message.getPhoto();
                                String fileId = photos.stream()
                                        .sorted((p1, p2) -> Integer.compare(p2
                                                .getFileSize(), p1.getFileSize()))
                                        .findFirst()
                                        .orElse(null)
                                        .getFileId();
                                currentAutopartsAd.setPhoto(fileId);
                                storage.addAdvertisement(profileLink, currentAutopartsAd);
                                adData.remove(chatId);
                                sendOut(chatId, "Объявление в категории " + currentAutopartsAd.getCategory() + " успешно создано!");
                            }
                        }else if (currentAd instanceof AdvertisementOfAnimals) {
                            AdvertisementOfAnimals currentAnimalsAd = (AdvertisementOfAnimals) currentAd;
                            currentAnimalsAd.setCategory();
                            if (currentAnimalsAd.getTitle().isEmpty()) {
                                currentAnimalsAd.setTitle(messageText);
                                sendOut(chatId, "Введите описание объявления");
                            } else if (currentAnimalsAd.getDescription().isEmpty()) {
                                currentAnimalsAd.setDescription(messageText);
                                sendOut(chatId, "Введите цену объявления в рублях");
                            } else if (currentAnimalsAd.getPrice() == 0) {
                                int price = Integer.parseInt(messageText);
                                currentAnimalsAd.setPrice(price);
                                sendOut(chatId, "Прикрипите фото");
                            } else if (currentAnimalsAd.getPhoto().isEmpty()) {
                                List<PhotoSize> photos = message.getPhoto();
                                String fileId = photos.stream()
                                        .sorted((p1, p2) -> Integer.compare(p2
                                                .getFileSize(), p1.getFileSize()))
                                        .findFirst()
                                        .orElse(null)
                                        .getFileId();
                                currentAnimalsAd.setPhoto(fileId);
                                storage.addAdvertisement(profileLink, currentAnimalsAd);
                                adData.remove(chatId);
                                sendOut(chatId, "Объявление в категории " + currentAnimalsAd.getCategory() + " успешно создано!");
                            }
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