package com.example.read_write_app_duan1.models;

public class Comment {
    private String uidComment, uidUser, uidBook, name, reply;
    private int imgAvatarUser;


    public Comment(String uidComment, String uidUser, String uidBook, String name, String reply, int imgAvatarUser) {
        this.uidComment = uidComment;
        this.uidUser = uidUser;
        this.uidBook = uidBook;
        this.name = name;
        this.reply = reply;
        this.imgAvatarUser = imgAvatarUser;
    }

    public String getUidComment() {
        return uidComment;
    }

    public void setUidComment(String uidComment) {
        this.uidComment = uidComment;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }

    public String getUidBook() {
        return uidBook;
    }

    public void setUidBook(String uidBook) {
        this.uidBook = uidBook;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public int getImgAvatarUser() {
        return imgAvatarUser;
    }

    public void setImgAvatarUser(int imgAvatarUser) {
        this.imgAvatarUser = imgAvatarUser;
    }
}
