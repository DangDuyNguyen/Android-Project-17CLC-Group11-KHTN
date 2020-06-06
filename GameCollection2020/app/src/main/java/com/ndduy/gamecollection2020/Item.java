package com.ndduy.gamecollection2020;

import android.graphics.drawable.Drawable;

public class Item {
    String name;
    String Images;
    int imageResource;

    public Item(){
        name = "";
        Images = "";
        imageResource = 0;
    }


    public Item(String name, String images) {
        this.name = name;
        Images = images;
    }

    public void setName(String name) {this.name = name;}

    public String getName() {
        return name;
    }

    public String getImages() {
        return Images;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
