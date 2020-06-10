package com.project.arcadedestroyer.Method;

public class Status {
    private String name;
    private int percentage;
    private int image;

    public Status(String name, int percentage, int image) {
        this.name = name;
        this.percentage = percentage;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
