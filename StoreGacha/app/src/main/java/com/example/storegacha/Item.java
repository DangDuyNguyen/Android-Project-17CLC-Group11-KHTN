package com.example.storegacha;

import android.app.Activity;

public class Item {
    String name;
    String Images;


    public Item(String name, String images) {
        this.name = name;
        Images = images;
    }

    public String getName() {
        return name;
    }

    public String getImages() {
        return Images;
    }

}
