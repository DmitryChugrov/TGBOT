package ru.tgbot;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DemoBot extends TelegramLongPollingBot {
    private static final String FILE_NAME = "data.json";
    private static Logger logger = Logger.getLogger(DemoBot.class);
    private final String BOT_TOKEN;
    private final String BOT_NAME;
    private Map<Long, Advertisement> adData;
    private Map<String, Advertisement> usersAd;
    Storage storage = new Storage();
    private ExecutorService executor;

    public DemoBot(String token, String chatId) {
        this.BOT_TOKEN = token;
        this.BOT_NAME = chatId;
        executor = Executors.newFixedThreadPool(10);
        adData = new HashMap<>();
        usersAd = new HashMap<>();
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
        executor.submit(() -> {
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
                } else if ("/createproperty".equals(messageText)) {
                    logger.info("Received /createproperty command from " + profileLink);
                    adData.put(chatId, new AdvertisementOfProperty("", "", "", 0, ""));
                    sendOut(chatId, "Введите заголовок объявления");
                } else if ("/createservices".equals(messageText)) {
                    logger.info("Received /createservices command from " + profileLink);
                    adData.put(chatId, new AdvertisementOfServices("", "", "", 0, ""));
                    sendOut(chatId, "Введите заголовок объявления");
                } else if ("/createjob".equals(messageText)) {
                    logger.info("Received /createjob command from " + profileLink);
                    adData.put(chatId, new AdvertisementOfJob("", "", "", 0, ""));
                    sendOut(chatId, "Введите заголовок объявления");
                } else if ("/createhobbyandentertainment".equals(messageText)) {
                    logger.info("Received /createhobbyandentertainment command from " + profileLink);
                    adData.put(chatId, new AdvertisementOfHobbyAndEntertainment("", "", "", 0, ""));
                    sendOut(chatId, "Введите заголовок объявления");
                } else if ("/createforhomeandgarden".equals(messageText)) {
                    logger.info("Received /createforhomeandgarden command from " + profileLink);
                    adData.put(chatId, new AdvertisementOfForHomeAndGarden("", "", "", 0, ""));
                    sendOut(chatId, "Введите заголовок объявления");
                } else if ("/createelectronics".equals(messageText)) {
                    logger.info("Received /createelectronics command from " + profileLink);
                    adData.put(chatId, new AdvertisementOfElectronics("", "", "", 0, ""));
                    sendOut(chatId, "Введите заголовок объявления");
                } else if ("/createautoparts".equals(messageText)) {
                    logger.info("Received /createautoparts command from " + profileLink);
                    adData.put(chatId, new AdvertisementOfAutoparts("", "", "", 0, ""));
                    sendOut(chatId, "Введите заголовок объявления");
                } else if ("/createanimals".equals(messageText)) {
                    logger.info("Received /createanimals command from " + profileLink);
                    adData.put(chatId, new AdvertisementOfAnimals("", "", "", 0, ""));
                    sendOut(chatId, "Введите заголовок объявления");
                } else if ("/createpersonalthing".equals(messageText)) {
                    logger.info("Received /createpersonalthing command from " + profileLink);
                    adData.put(chatId, new AdvertisementOfPersonalThings("", "", "", 0, ""));
                    sendOut(chatId, "Введите заголовок объявления");
                } else {
                    if (adData.containsKey(chatId)) {
                        Advertisement currentAd = adData.get(chatId);
                        if (currentAd instanceof AdvertisementOfTransport) {
                            AdvertisementOfTransport currentTransportAd = (AdvertisementOfTransport) currentAd;
                            processAdvertisement("Транспорт", currentTransportAd, chatId, messageText, message, profileLink);
                        } else if (currentAd instanceof AdvertisementOfProperty) {
                            AdvertisementOfProperty currentPropertyAd = (AdvertisementOfProperty) currentAd;
                            processAdvertisement("Недвижимость", currentPropertyAd, chatId, messageText, message, profileLink);
                        } else if (currentAd instanceof AdvertisementOfServices) {
                            AdvertisementOfServices currentServicesAd = (AdvertisementOfServices) currentAd;
                            processAdvertisement("Услуги", currentServicesAd, chatId, messageText, message, profileLink);
                        } else if (currentAd instanceof AdvertisementOfJob) {
                            AdvertisementOfJob currentJobAd = (AdvertisementOfJob) currentAd;
                            processAdvertisement("Работа", currentJobAd, chatId, messageText, message, profileLink);
                        } else if (currentAd instanceof AdvertisementOfHobbyAndEntertainment) {
                            AdvertisementOfHobbyAndEntertainment currentHobbyAndEntertainmentAd = (AdvertisementOfHobbyAndEntertainment) currentAd;
                            processAdvertisement("Хобби и развлечения", currentHobbyAndEntertainmentAd, chatId, messageText, message, profileLink);
                        } else if (currentAd instanceof AdvertisementOfForHomeAndGarden) {
                            AdvertisementOfForHomeAndGarden currentForHomeAndGardenAd = (AdvertisementOfForHomeAndGarden) currentAd;
                            processAdvertisement("Для дома и сада", currentForHomeAndGardenAd, chatId, messageText, message, profileLink);
                        } else if (currentAd instanceof AdvertisementOfElectronics) {
                            AdvertisementOfElectronics currentElectronicsAd = (AdvertisementOfElectronics) currentAd;
                            processAdvertisement("Электроника", currentElectronicsAd, chatId, messageText, message, profileLink);
                        } else if (currentAd instanceof AdvertisementOfAutoparts) {
                            AdvertisementOfAutoparts currentAutopartsAd = (AdvertisementOfAutoparts) currentAd;
                            processAdvertisement("Автозапчасти", currentAutopartsAd, chatId, messageText, message, profileLink);
                        } else if (currentAd instanceof AdvertisementOfAnimals) {
                            AdvertisementOfAnimals currentAnimalsAd = (AdvertisementOfAnimals) currentAd;
                            processAdvertisement("Животные", currentAnimalsAd, chatId, messageText, message, profileLink);
                        } else if (currentAd instanceof AdvertisementOfPersonalThings) {
                            AdvertisementOfPersonalThings currentPersonalThingAd = (AdvertisementOfPersonalThings) currentAd;
                            processAdvertisement("Личные вещи", currentPersonalThingAd, chatId, messageText, message, profileLink);
                        }
                    }
                }
                if ("/view".equals(messageText)) {
                    for (Map.Entry<Advertisement, String> entry : storage.entrySet()) {
                        String prLinks = entry.getValue();
                        Advertisement ad = entry.getKey();
                        String adInfo = "Пользователь: " + prLinks + "\n" + "Категория: " + ad.getCategory() + "\n" + "Заголовок: " + ad.getTitle() + "\n" +
                                "Описание: " + ad.getDescription() + "\n" + "Цена (руб): " + ad.getPrice();
                        sendPhoto(chatId, ad.getPhoto(), adInfo);
                    }
                }
            }
        });
    }


    private void sendPhoto(long chatId, String fileId, String caption) {
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

    private void processAdvertisement(String category, Advertisement currentAd, long chatId, String messageText, Message message, String profileLink) {
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
            try {
                // Получаем информацию о файле
                GetFile getFile = new GetFile();
                getFile.setFileId(fileId);
                File file = execute(getFile);

                // Сохраняем фото на диск
                java.io.File photoFile = customDownloadFile(file);

                // Получаем ссылку на сохраненное фото
                String photoLink = "D:\\TGBOT\\photos\\" + photoFile.getName();

                // Сохраняем ссылку в файл json
                currentAd.setPhoto(photoLink);

                adData.remove(chatId);
                adData.put(chatId, currentAd);
                usersAd.put(profileLink, currentAd);
                saveData(usersAd);
                storage.addAdvertisement(profileLink, currentAd);

                sendOut(chatId, "Объявление в категории " + currentAd.getCategory() + " успешно создано!");
            } catch (TelegramApiException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private java.io.File customDownloadFile(File file) throws TelegramApiException, IOException {
        String filePath = file.getFilePath();
        java.io.File downloadedFile = downloadFile(filePath);
        // Указываем путь для сохранения фото на диск
        java.io.File savedFile = new java.io.File("D:\\TGBOT\\photos\\" + downloadedFile.getName());
        // Копируем скачанный файл на указанный путь
        Files.copy(downloadedFile.toPath(), savedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return savedFile;
    }
    private void saveData(Map<String, Advertisement> data) {
        try (Reader reader = new FileReader(FILE_NAME)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<Advertisement>>>() {}.getType();
            Map<String, List<Advertisement>> existingData = gson.fromJson(reader, type);

            for (Map.Entry<String, Advertisement> entry : data.entrySet()) {
                String profileLink = entry.getKey();
                Advertisement advertisement = entry.getValue();

                if (existingData.containsKey(profileLink)) {
                    List<Advertisement> advertisements = existingData.get(profileLink);
                    advertisements.add(advertisement);
                } else {
                    List<Advertisement> advertisements = new ArrayList<>();
                    advertisements.add(advertisement);
                    existingData.put(profileLink, advertisements);
                }
            }

            try (Writer writer = new FileWriter(FILE_NAME)) {
                gson.toJson(existingData, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}