package ru.tgbot;

import java.util.List;

public class User {
        private String profileLink;
        private long chatID;
        private List<Advertisement> userAds;
        private List<Advertisement> viewedAds;


        public User(String profileLink, long chatID) {
                this.profileLink = profileLink;
                this.chatID = chatID;
        }

        public void addUserAds(Advertisement advertisement) {
                userAds.add(advertisement);
        }

        public List<Advertisement> getUserAds() {
                return userAds;
        }
}