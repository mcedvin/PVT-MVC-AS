package com.example.edvin.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ChallengeAccepted {

    private Challenge challenge;

    private Date date;


    @SerializedName("body")
    private String text;

    public Challenge getChallenge() {
        return challenge;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }
}
