package ru.tgbot;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class DemoBot extends TelegramLongPollingBot {
    private static Logger logger = Logger.getLogger(DemoBot.class);
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
                logger.info("Received /start command from " + profileLink);
                sendProfileInfo(chatId, username, profileLink);
            }
            if ("/createtransport".equals(messageText)) {
                logger.info("Received /createtransport command from " + profileLink);
                adData.put(chatId, new AdvertisementOfTransport("", "", "", 0, ""));
                sendOut(chatId, "Введите заголовок объявления");
            }else if ("/createproperty".equals(messageText)) {
                logger.info("Received /createproperty command from " + profileLink);
                    adData.put(chatId, new AdvertisementOfProperty("", "", "", 0, ""));
                    sendOut(chatId, "Введите заголовок объявления");
            }
            else if ("/createservices".equals(messageText)) {
                logger.info("Received /createservices command from " + profileLink);
                adData.put(chatId, new AdvertisementOfServices("", "", "", 0, ""));
                sendOut(chatId, "Введите заголовок объявления");
            }
            else if ("/createjob".equals(messageText)) {
                logger.info("Received /createjob command from " + profileLink);
                adData.put(chatId, new AdvertisementOfJob("", "", "", 0, ""));
                sendOut(chatId, "Введите заголовок объявления");
            }
            else if ("/createhobbyandentertainment".equals(messageText)) {
                logger.info("Received /createhobbyandentertainment command from " + profileLink);
                adData.put(chatId, new AdvertisementOfHobbyAndEntertainment("", "", "", 0, ""));
                sendOut(chatId, "Введите заголовок объявления");
            }
            else if ("/createforhomeandgarden".equals(messageText)) {
                logger.info("Received /createforhomeandgarden command from " + profileLink);
                adData.put(chatId, new AdvertisementOfForHomeAndGarden("", "", "", 0, ""));
                sendOut(chatId, "Введите заголовок объявления");
            }
            else if ("/createelectronics".equals(messageText)) {
                logger.info("Received /createelectronics command from " + profileLink);
                adData.put(chatId, new AdvertisementOfElectronics("", "", "", 0, ""));
                sendOut(chatId, "Введите заголовок объявления");
            }
            else if ("/createautoparts".equals(messageText)) {
                logger.info("Received /createautoparts command from " + profileLink);
                adData.put(chatId, new AdvertisementOfAutoparts("", "", "", 0, ""));
                sendOut(chatId, "Введите заголовок объявления");
            }
            else if ("/createanimals".equals(messageText)) {
                logger.info("Received /createanimals command from " + profileLink);
                adData.put(chatId, new AdvertisementOfAnimals("", "", "", 0, ""));
                sendOut(chatId, "Введите заголовок объявления");
            }
            else if ("/createpersonalthing".equals(messageText)) {
                logger.info("Received /createpersonalthing command from " + profileLink);
                adData.put(chatId, new AdvertisementOfPersonalThings("", "", "", 0, ""));
                sendOut(chatId, "Введите заголовок объявления");
            }
            else {
                    if (adData.containsKey(chatId)) {
                        Advertisement currentAd = adData.get(chatId);
                        if (currentAd instanceof AdvertisementOfTransport) {
                            AdvertisementOfTransport currentTransportAd = (AdvertisementOfTransport) currentAd;
                           processAdvertisement("Транспорт",currentTransportAd,chatId,messageText,message,profileLink);
                        } else if (currentAd instanceof AdvertisementOfProperty) {
                            AdvertisementOfProperty currentPropertyAd = (AdvertisementOfProperty) currentAd;
                           processAdvertisement("Недвижимость",currentPropertyAd,chatId,messageText,message,profileLink);
                        }else if (currentAd instanceof AdvertisementOfServices) {
                            AdvertisementOfServices currentServicesAd = (AdvertisementOfServices) currentAd;
                            processAdvertisement("Услуги", currentServicesAd, chatId, messageText, message, profileLink);
                        }else if (currentAd instanceof AdvertisementOfJob) {
                            AdvertisementOfJob currentJobAd = (AdvertisementOfJob) currentAd;
                            processAdvertisement("Работа", currentJobAd, chatId, messageText, message, profileLink);
                        }
                        else if (currentAd instanceof AdvertisementOfHobbyAndEntertainment) {
                            AdvertisementOfHobbyAndEntertainment currentHobbyAndEntertainmentAd = (AdvertisementOfHobbyAndEntertainment) currentAd;
                           processAdvertisement("Хобби и развлечения", currentHobbyAndEntertainmentAd, chatId, messageText, message, profileLink);
                        }else if (currentAd instanceof AdvertisementOfForHomeAndGarden) {
                            AdvertisementOfForHomeAndGarden currentForHomeAndGardenAd = (AdvertisementOfForHomeAndGarden) currentAd;
                            processAdvertisement("Для дома и сада", currentForHomeAndGardenAd, chatId, messageText, message, profileLink);
                        }else if (currentAd instanceof AdvertisementOfElectronics) {
                            AdvertisementOfElectronics currentElectronicsAd = (AdvertisementOfElectronics) currentAd;
                            processAdvertisement("Электроника", currentElectronicsAd, chatId, messageText, message, profileLink);
                        }else if (currentAd instanceof AdvertisementOfAutoparts) {
                            AdvertisementOfAutoparts currentAutopartsAd = (AdvertisementOfAutoparts) currentAd;
                            processAdvertisement("Автозапчасти", currentAutopartsAd, chatId, messageText, message, profileLink);
                        }else if (currentAd instanceof AdvertisementOfAnimals) {
                            AdvertisementOfAnimals currentAnimalsAd = (AdvertisementOfAnimals) currentAd;
                            processAdvertisement("Животные", currentAnimalsAd, chatId, messageText, message, profileLink);
                        }else if (currentAd instanceof AdvertisementOfPersonalThings){
                            AdvertisementOfPersonalThings currentPersonalThingAd = (AdvertisementOfPersonalThings) currentAd;
                            processAdvertisement("Личные вещи", currentPersonalThingAd,chatId, messageText, message, profileLink);
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
    private void processAdvertisement(String category,Advertisement currentAd, long chatId, String messageText, Message message, String profileLink){
        currentAd.setCategory(category);
        if (currentAd.getTitle().isEmpty()) {
            currentAd.setTitle(messageText);
            sendOut(chatId, "Введите описание объявления");
        } else if (currentAd.getDescription().isEmpty()) {
            currentAd.setDescription(messageText);
            sendOut(chatId, "Введите цену объявления в рублях");
        } else if (currentAd.getPrice() == 0) {
            int price = Integer.parseInt(messageText);
            currentAd.setPrice(price);
            sendOut(chatId, "Прикрепите фото");
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
            sendOut(chatId, "Объявление в категории " + currentAd.getCategory() + " успешно создано!");
        }
    }
}