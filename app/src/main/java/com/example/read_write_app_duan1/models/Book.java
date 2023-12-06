package com.example.read_write_app_duan1.models;

public class Book {
    private String id;
    private String name;
    private String author;
    private String discription;
    private String chapter;
    private String status;
    private String type;
    private String comment;
    private String image;
    private String content;
    private String write;

    public String getWrite() {
        return write;
    }

    public void setWrite(String write) {
        this.write = write;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Book(String id, String name, String author, String discription, String chapter, String status, String type) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.discription = discription;
        this.chapter = chapter;
        this.status = status;
        this.type = type;
        this.comment = comment;
        this.image = image;
    }

    public Book(String id, String name, String image, String pdfUrl, String category, int checkBook, int status) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Book(String name) {
        this.name = name;
        this.image = image;
    }

    public Book(String content,String image) {
        this.content = content;
        this.image = image;
    }
    public Book(String name, String type, String discription, String content, String image) {
        this.name = name;
        this.image = image;
        this.type = type;
        this.discription = discription;
        this.content = content;

    public Book(String name, String image, String id) {
        this.name = name;
        this.image = image;
        this.id = id;

    }

    public Book() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
