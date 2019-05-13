package com.example.edvin.app.models;

import com.google.gson.annotations.SerializedName;

public class UserAccount {
    private int id;

    private String password;

    @SerializedName("body")
    private String text;

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getText() {
        return text;
    }
}
