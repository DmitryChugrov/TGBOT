package ru.tgbot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main (String[] args) throws TelegramApiException {
        String BOT_TOKEN = "6300541666:AAFFNbzwrQ3B1d0BdxQhHAEkRzeS0h7pwlM";
        String BOT_NAME = "boardOf_Ads_bot";
        DemoBot bot = new DemoBot(BOT_TOKEN, BOT_NAME);
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
