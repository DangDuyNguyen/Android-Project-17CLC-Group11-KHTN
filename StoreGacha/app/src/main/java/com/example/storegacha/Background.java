package com.example.storegacha;

public class Background {
    public Background(String name, int img) {
        this.name = name;
        Img = img;
    }

    String name;
    int Img;

    public String getName() {
        return name;
    }

    public int getImg() {
        return Img;
    }
}
