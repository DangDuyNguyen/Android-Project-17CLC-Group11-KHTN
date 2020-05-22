package com.ndduy.gamecollection2020;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;

import androidx.core.content.ContextCompat;

public class Status {
    private String name;
    private int percentage;
    private double decrease_rate;
    private int image;

    public Status(String name, int percentage, double decrease_rate, int image) {
        this.name = name;
        this.percentage = percentage;
        this.decrease_rate = decrease_rate;
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

    public double getDecrease_rate() {
        return decrease_rate;
    }

    public void setDecrease_rate(double decrease_rate) {
        this.decrease_rate = decrease_rate;
    }
}
