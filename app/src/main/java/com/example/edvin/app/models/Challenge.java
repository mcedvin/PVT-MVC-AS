package com.example.edvin.app.models;

import android.media.Image;

import com.google.gson.annotations.SerializedName;

public class Challenge {


    private String name;

    private String description;
    private int duration; //timestamp?
    private byte[] image; //default image?
    private ChallengeCategory challengeCategory;


    @SerializedName("body")
    private String text;


    public Challenge(String n,String d,int duration,byte[] img, ChallengeCategory challengeCategory){
        name =n;
        description=d;
        this.duration=duration;
        image=img;
        this.challengeCategory= challengeCategory;
    }




    public byte[] getImage() {
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

    public void setImage(byte[] image) {
        this.image = image;
    }

    public ChallengeCategory getChallengeCategory() {
        return challengeCategory;
    }

    public String getText() {
        return text;
    }

    public enum ChallengeCategory {
        //havet naturen djuren luften
        HAVET,
        NATUREN,
        DJUREN,
        LUFTEN
    }


}
