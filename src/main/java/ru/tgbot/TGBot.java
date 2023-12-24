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


public class TGBot extends TelegramLongPollingBot {
    // Измените ссылку файла data.json
    private static final String FILE_NAME = "/root/TGBOT/TGBOT/target/data.json";
    private static Logger logger = Logger.getLogger(TGBot.class);
    private final String BOT_TOKEN;
    private final String BOT_NAME;
    private Map<Long, Advertisement> adData;
    private Map<String, Advertisement> usersAd;
    private ExecutorService executor;
    public TGBot(String token, String name) {
        this.BOT_TOKEN = token;
        this.BOT_NAME = name;
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
                            processOfCreatingAdvertisement("Транспорт", currentTransportAd, chatId, messageText, message, profileLink);
                        } else if (currentAd instanceof AdvertisementOfProperty) {
                            AdvertisementOfProperty currentPropertyAd = (AdvertisementOfProperty) currentAd;
                            processOfCreatingAdvertisement("Недвижимость", currentPropertyAd, chatId, messageText, message, profileLink);
                        } else if (currentAd instanceof AdvertisementOfServices) {
                            AdvertisementOfServices currentServicesAd = (AdvertisementOfServices) currentAd;
                            processOfCreatingAdvertisement("Услуги", currentServicesAd, chatId, messageText, message, profileLink);
                        } else if (currentAd instanceof AdvertisementOfJob) {
                            AdvertisementOfJob currentJobAd = (AdvertisementOfJob) currentAd;
                            processOfCreatingAdvertisement("Работа", currentJobAd, chatId, messageText, message, profileLink);
                        } else if (currentAd instanceof AdvertisementOfHobbyAndEntertainment) {
                            AdvertisementOfHobbyAndEntertainment currentHobbyAndEntertainmentAd = (AdvertisementOfHobbyAndEntertainment) currentAd;
                            processOfCreatingAdvertisement("Хобби и развлечения", currentHobbyAndEntertainmentAd, chatId, messageText, message, profileLink);
                        } else if (currentAd instanceof AdvertisementOfForHomeAndGarden) {
                            AdvertisementOfForHomeAndGarden currentForHomeAndGardenAd = (AdvertisementOfForHomeAndGarden) currentAd;
                            processOfCreatingAdvertisement("Для дома и сада", currentForHomeAndGardenAd, chatId, messageText, message, profileLink);
                        } else if (currentAd instanceof AdvertisementOfElectronics) {
                            AdvertisementOfElectronics currentElectronicsAd = (AdvertisementOfElectronics) currentAd;
                            processOfCreatingAdvertisement("Электроника", currentElectronicsAd, chatId, messageText, message, profileLink);
                        } else if (currentAd instanceof AdvertisementOfAutoparts) {
                            AdvertisementOfAutoparts currentAutopartsAd = (AdvertisementOfAutoparts) currentAd;
                            processOfCreatingAdvertisement("Автозапчасти", currentAutopartsAd, chatId, messageText, message, profileLink);
                        } else if (currentAd instanceof AdvertisementOfAnimals) {
                            AdvertisementOfAnimals currentAnimalsAd = (AdvertisementOfAnimals) currentAd;
                            processOfCreatingAdvertisement("Животные", currentAnimalsAd, chatId, messageText, message, profileLink);
                        } else if (currentAd instanceof AdvertisementOfPersonalThings) {
                            AdvertisementOfPersonalThings currentPersonalThingAd = (AdvertisementOfPersonalThings) currentAd;
                            processOfCreatingAdvertisement("Личные вещи", currentPersonalThingAd, chatId, messageText, message, profileLink);
                        }
                    }
                }
                if ("/view".equals(messageText)) {
                    logger.info("Received /view command from " + profileLink);
                    showAds(chatId);
                }
                if ("/viewmy".equals(messageText)) {
                    logger.info("Received /viewmy command from " + profileLink);
                    showMyAds(profileLink, chatId);
                }if ("/help".equals(messageText)){
                    logger.info("Received /help command from " + profileLink);
                    sendOut(chatId, "Список команд для работы с ботом: \n" +
                            "/start - Старт работы с ботом\n" +
                            "/createtransport - Создание объявлений в категории \"Транспорт\"\n" +
                            "/createproperty - Создание объявлений в категории \"Недвижимость\" \n" +
                            "/createservices - Создание объявлений в категории \"Услуги\" \n" +
                            "/createpersonalthings - Создание объявлений в категории \"Личные вещи\" \n" +
                            "/createjob - Создание объявлений в категории \"Работа\" \n" +
                            "/createhobbyandentertainment - Создание объявлений в категории \"Хобби и отдых\"\n" +
                            "/createforhomeandgarden - Создание объявлений в категории \"Для дома и сада\"\n" +
                            "/createelectronics - Создание объявлений в категории \"Электроника\"\n" +
                            "/createautoparts - Создание объявлений в категории \"Запчасти\"\n" +
                            "/createanimals - Создание объявлений в категории \"Животные\"\n" +
                            "/view - Просмотр объявлений\n" +
                            "/viewmy - Просмотр собственных объявлений\n" +
                            "/deletemyad - Удаление собственного объявления \n");
                }
                if ("/deletemyad".equals(messageText)) {
                    logger.info("Received /deletemyad command from " + profileLink);
                    showMyAds(profileLink,chatId);
                    sendOut(chatId, "Введите индекс объявления. Для выхода из этой функции введите \" -1 \"");
                } else {
                    int indexToRemove;
                    indexToRemove = Integer.parseInt(messageText);
                    deleteAdvertisement(profileLink,chatId,indexToRemove);
                }
            }
        });
    }


    private void sendPhoto(long chatId, String fileId, String caption) {
        SendPhoto response = new SendPhoto();
        response.setChatId(String.valueOf(chatId));
        response.setPhoto(new InputFile(new java.io.File(fileId)));
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

    private void processOfCreatingAdvertisement(String category, Advertisement currentAd, long chatId, String messageText, Message message, String profileLink) {
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
                GetFile getFile = new GetFile();
                getFile.setFileId(fileId);
                File file = execute(getFile);
                java.io.File photoFile = customDownloadFile(file);
                // Измените ссылку папки photos
                String photoLink = "/root/TGBOT/TGBOT/target/photos/" + photoFile.getName();
                currentAd.setPhoto(photoLink);
                adData.remove(chatId);
                usersAd.put(profileLink, currentAd);
                saveData(usersAd);
                sendOut(chatId, "Объявление в категории " + currentAd.getCategory() + " успешно создано!");
                logger.info("User "+ profileLink + " created the ad successfully in the category " + currentAd.getCategory());
            } catch (TelegramApiException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private java.io.File customDownloadFile(File file) throws TelegramApiException, IOException {
        String filePath = file.getFilePath();
        java.io.File downloadedFile = downloadFile(filePath);
        // Измените ссылку папки photos
        java.io.File savedFile = new java.io.File("/root/TGBOT/TGBOT/target/photos/" + downloadedFile.getName() + ".jpg");
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

    private void showAds(long chatId) {
        try (Reader reader = new FileReader(FILE_NAME)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<Advertisement>>>(){}.getType();
            Map<String, List<Advertisement>> data = gson.fromJson(reader, type);
            for (String key : data.keySet()) {
                List<Advertisement> ads = data.get(key);
                for (Advertisement ad : ads) {
                    String adInfo = "Пользователь: " + key + "\n" + "Категория: " + ad.getCategory() + "\n" + "Заголовок: " + ad.getTitle() + "\n" +
                            "Описание: " + ad.getDescription() + "\n" + "Цена (руб): " + ad.getPrice();
                    sendPhoto(chatId, ad.getPhoto(), adInfo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showMyAds(String profileLink, long chatId) {
        try (Reader reader = new FileReader(FILE_NAME)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<Advertisement>>>() {}.getType();
            Map<String, List<Advertisement>> existingData = gson.fromJson(reader, type);

            if (existingData.containsKey(profileLink)) {
                List<Advertisement> advertisements = existingData.get(profileLink);

                sendOut(chatId,"Ваши объявления: ");
                for (int i = 0; i < advertisements.size(); i++) {
                    Advertisement ad = advertisements.get(i);
                    String adInfo = "Индекс объявления: " + i +"\n" +"Категория: " + ad.getCategory() + "\n" + "Заголовок: " + ad.getTitle() + "\n" +
                            "Описание: " + ad.getDescription() + "\n" + "Цена (руб): " + ad.getPrice();
                    sendPhoto(chatId, ad.getPhoto(), adInfo);
                }
            } else {
                sendOut(chatId, "У вас нет объявлений.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void deleteAdvertisement(String profileLink, long chatId, int indexToRemove) {
        try (Reader reader = new FileReader(FILE_NAME)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<Advertisement>>>() {}.getType();
            Map<String, List<Advertisement>> existingData = gson.fromJson(reader, type);
            if (existingData.containsKey(profileLink)) {
                List<Advertisement> advertisements = existingData.get(profileLink);
                if (indexToRemove >= 0 && indexToRemove < advertisements.size()) {
                    String photoFileName = advertisements.get(indexToRemove).getPhoto();
                    java.io.File photoFile = new java.io.File(photoFileName);
                    if (photoFile.exists()) {
                        photoFile.delete();
                    }
                    advertisements.remove(indexToRemove);
                    existingData.put(profileLink, advertisements);
                    try (Writer writer = new FileWriter(FILE_NAME)) {
                        gson.toJson(existingData, writer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    sendOut(chatId, "Объявление успешно удалено.");
                }else if(indexToRemove == -1){
                    sendOut(chatId, "Выполнен выход из функции удаления объявления");
                }
            } else {
                sendOut(chatId, "Не найдено объявлений для данной ссылки на профиль.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
