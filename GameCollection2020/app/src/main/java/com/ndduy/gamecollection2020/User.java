package com.ndduy.gamecollection2020;

import java.util.ArrayList;

public class User {
    private String account;
    private String username;
    private String name;
    private String Coin;
    private Status Hungriness;
    private Status Flattering;
    private Status Sleepiness;
    private Status Mood;
    private ArrayList<String> char_list;
    private ArrayList<String> bg_list;

    public User() {
        account = "";
        name = "My name is...";
        Coin = "0";
        Hungriness = new Status("Hungriness", 100, R.drawable.green_hungry_status_button);
        Flattering = new Status("Flattering", 100, R.drawable.green_bathroom_status_button);
        Sleepiness = new Status("Sleepiness", 100, R.drawable.green_sleepy_status_button);
        Mood = new Status("Mood", 100, R.drawable.green_mood_status_button);

        bg_list = new ArrayList<>();
        char_list = new ArrayList<>();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUser(User user){
        this.name = user.getName();
        this.Coin = user.getCoin();
        this.Hungriness = user.getHungriness();
        this.Flattering = user.getFlattering();
        this.Sleepiness = user.getSleepiness();
        this.Mood = user.getMood();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoin() {
        return Coin;
    }

    public void setCoin(String coin) {
        Coin = coin;
    }

    public Status getHungriness() {
        return Hungriness;
    }

    public void setHungriness(Status hungriness) {
        Hungriness = hungriness;
    }

    public Status getFlattering() {
        return Flattering;
    }

    public void setFlattering(Status flattering) {
        Flattering = flattering;
    }

    public Status getSleepiness() {
        return Sleepiness;
    }

    public void setSleepiness(Status sleepiness) {
        Sleepiness = sleepiness;
    }

    public Status getMood() {
        return Mood;
    }

    public void setMood(Status mood) {
        Mood = mood;
    }

    public ArrayList<String> getChar_list() {
        return char_list;
    }

    public void setChar_list(ArrayList<String> char_list) {
        this.char_list = char_list;
    }

    public ArrayList<String> getBg_list() {
        return bg_list;
    }

    public void setBg_list(ArrayList<String> bg_list) {
        this.bg_list = bg_list;
    }
}
