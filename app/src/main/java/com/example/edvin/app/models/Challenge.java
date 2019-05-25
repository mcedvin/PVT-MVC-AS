package com.example.edvin.app.models;

import android.media.Image;

import com.google.gson.annotations.SerializedName;

public class Challenge {


    private String name;

    private String description;
    private int duration; //timestamp?
    private Image image; //default image?


    @SerializedName("body")
    private String text;


    public Challenge(String n,String d,int duration,Image img){
        name =n;
        description=d;
        this.duration=duration;
        image=img;
    }


    public String getText() {
        return text;
    }

    public Image getImage() {
        return image;
    }

    public int getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setImage(Image image) {
        this.image = image;
    }


}
