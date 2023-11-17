package com.example.read_write_app_duan1.models;

public class LibraryRead {

    String chapter, name, author, image, type;

    public LibraryRead() {
    }

    public LibraryRead(String chapter, String name, String author, String image, String type) {
        this.chapter = chapter;
        this.name = name;
        this.author = author;
        this.image = image;
        this.type = type;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
