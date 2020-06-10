package com.project.arcadedestroyer.Method;

public class Item {
    int id;
    String name;
    String Images;
    int type;
    int ImageResources;

    public Item(){
        id = 0;
        name = "";
        Images = "";
        type = 0;
    }

    public Item(String name, String images) {
        this.name = name;
        Images = images;
    }

    public Item(int id, String name, String images, int type) {
        this.id = id;
        this.name = name;
        Images = images;
        this.type = type;
    }

    public void setName(String name) {this.name = name;}

    public String getName() {
        return name;
    }

    public String getImages() {
        return Images;
    }

    public void setImages(String image) { this.Images = image; }

    public int getType() { return type; }

    public void setType(int t) { this.type = t; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResource() {
        return ImageResources;
    }

    public void setResource(int resource) {
        this.ImageResources = resource;
    }
}
