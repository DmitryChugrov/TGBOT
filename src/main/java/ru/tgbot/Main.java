package ru.tgbot;

import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main (String[] args) throws TelegramApiException {
        Dotenv dotenv = Dotenv.load();
        String botName = dotenv.get("BOT_NAME");
        String botToken = dotenv.get("BOT_TOKEN");
        DemoBot bot = new DemoBot(botToken, botName);
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot);
            System.out.println("Starting...");
            System.out.println();
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}
