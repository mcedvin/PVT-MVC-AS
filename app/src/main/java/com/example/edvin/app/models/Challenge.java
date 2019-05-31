package com.example.edvin.app.models;

import android.media.Image;

import com.google.gson.annotations.SerializedName;

public class Challenge {


    private String name;

    private String description;
    private int duration; //timestamp?
    private byte[] image; //default image?
    //private ChallengeCategory challengeCategory;
    private int challengeCategory;


    @SerializedName("body")
    private String text;


    public Challenge(String n,String d,int duration,byte[] img, int challengeCategory){
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

    public int getChallengeCategory() {
        return challengeCategory;
    }

    public String getText() {
        return text;
    }

    public boolean isEqual(Challenge c){

        return name.equals(c.getName());
    }

    /*
    public enum ChallengeCategory {
        //havet naturen djuren luften
        HAVET,      =1
        NATUREN,    =2
        DJUREN,     =3
        LUFTEN      =4
    }

*/
}
