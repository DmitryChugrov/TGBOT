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
    private static final String FILE_NAME = "D:\\TGBOT\\data.json";
    private static Logger logger = Logger.getLogger(DemoBot.class);
    private final String BOT_TOKEN;
    private final String BOT_NAME;
    private Map<Long, Advertisement> adData;
    private Map<String, Advertisement> usersAd;
    Storage storage = new Storage();
    private ExecutorService executor;
    private final List<String> WHITE_LIST = new ArrayList<>();

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
                    logger.info("Received /view command from " + profileLink);
                    loadData(chatId);
                }
                if ("/viewmy".equals(messageText)) {
                    logger.info("Received /viewmy command from " + profileLink);
                    getAdvertisements(profileLink, chatId);
                }if ("/help".equals(messageText)){
                    logger.info("Received /help command from " + profileLink);
                    sendOut(chatId, "Список команд для работы с ботом: \n" +
                            "start - Старт работы с ботом\n" +
                            "createtransport - Создание в объявлений в категории \"Транспорт\"\n" +
                            "createproperty - Создание в объявлений в категории \"Недвижимость\" \n" +
                            "createservices - Создание в объявлений в категории \"Услуги\" \n" +
                            "createpersonalthings - Создание в объявлений в категории \"Личные вещи\" \n" +
                            "createjob - Создание в объявлений в категории \"Работа\" \n" +
                            "createhobbyandentertainment - Создание в объявлений в категории \"Хобби и отдых\"\n" +
                            "createforhomeandgarden - Создание в объявлений в категории \"Для дома и сада\"\n" +
                            "createelectronics - Создание в объявлений в категории \"Электроника\"\n" +
                            "createautoparts - Создание в объявлений в категории \"Запчасти\"\n" +
                            "createanimals - Создание в объявлений в категории \"Животные\"\n" +
                            "view - Просмотр объявлений\n" +
                            "viewmy - Просмотр собственных объявлений\n" +
                            "deletemyad - Удаление собственного объявления \n");
                }
                if ("/deletemyad".equals(messageText)) {
                    logger.info("Received /deletemyad command from " + profileLink);
                    getAdvertisements(profileLink,chatId);
                    sendOut(chatId, "Введите индекс объявления. Для выхода из этой функции введите \" -1 \"");
                } else {
                    int indexToRemove = 0;
                    indexToRemove = Integer.parseInt(messageText);
                    deleteAdvertisements(profileLink,chatId,indexToRemove);
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
                currentAd.setPhoto(photoLink);
                adData.remove(chatId);
                usersAd.put(profileLink, currentAd);
                saveData(usersAd);

                sendOut(chatId, "Объявление в категории " + currentAd.getCategory() + " успешно создано!");
            } catch (TelegramApiException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private java.io.File customDownloadFile(File file) throws TelegramApiException, IOException {
        String filePath = file.getFilePath();
        java.io.File downloadedFile = downloadFile(filePath);
        java.io.File savedFile = new java.io.File("D:\\TGBOT\\photos\\" + downloadedFile.getName() + ".jpg");
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

    private void loadData(long chatId) {
        try (Reader reader = new FileReader(FILE_NAME)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<Advertisement>>>() {}.getType();
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
    private void getAdvertisements(String profileLink, long chatId) {
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
    private void deleteAdvertisements(String profileLink, long chatId,int indexToRemove) {
        try (Reader reader = new FileReader(FILE_NAME)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<Advertisement>>>() {}.getType();
            Map<String, List<Advertisement>> existingData = gson.fromJson(reader, type);

            if (existingData.containsKey(profileLink)) {
                List<Advertisement> advertisements = existingData.get(profileLink);
                if (indexToRemove >= 0 && indexToRemove < advertisements.size()) {
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
                }else {
                    sendOut(chatId, "Неверный индекс объявления.");
                }
            } else {
                sendOut(chatId, "Не найдено объявлений для данной ссылки на профиль.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}