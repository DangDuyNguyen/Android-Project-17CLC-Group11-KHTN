package com.ndduy.gamecollection2020;

public class User {

    private String name;
    private String Coin;
    private Status Hungriness;
    private Status Flattering;
    private Status Sleepiness;
    private Status Mood;

    User() {
        name = "My name is...";
        Coin = "0";
        Hungriness = new Status("Hungriness", 100, 0.2,  R.drawable.green_hungry_status_button);
        Flattering = new Status("Flattering", 100, 0.7, R.drawable.green_bathroom_status_button);
        Sleepiness = new Status("Sleepiness", 100, 1, R.drawable.green_sleepy_status_button);
        Mood = new Status("Mood", 100, 0.8, R.drawable.green_mood_status_button);
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
}
