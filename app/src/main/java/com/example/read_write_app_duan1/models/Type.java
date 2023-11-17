package com.example.read_write_app_duan1.models;

public class Type {
    String image;
    String type;

    public Type() {
    }

    public Type(String image, String type) {
        this.image = image;
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
