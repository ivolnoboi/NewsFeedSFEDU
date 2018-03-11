package ru.sfedu.lereena;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class ModelItem {

    private String imgURL;
    private String date;
    private String text;
    private Long like;
    private ArrayList<String> attachmentURLs;
    //int imgId;

    public ModelItem(@NonNull String imgURL, @NonNull String date, @NonNull String text,
              @NonNull Long like, ArrayList<String> attachmentURLs) {
        this.imgURL = imgURL;
        this.date = date;
        this.text = text;
        this.like = like;
        this.attachmentURLs = attachmentURLs;
    }

    public String getPhotoURL() {
        return imgURL;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public Long getLike() {
        return like;
    }

    public ArrayList<String> getAttachmentURLs() {
        return attachmentURLs;
    }
}
