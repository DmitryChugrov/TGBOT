package ru.tgbot;

import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User {
        private String profileLink;
        private long chatID;
        private List<Advertisement> userAds;
        private List<Advertisement> viewedAds;


        public User(String profileLink, long chatID) {
                this.profileLink = profileLink;
                this.chatID = chatID;
                this.userAds = new ArrayList<>();
        }

}