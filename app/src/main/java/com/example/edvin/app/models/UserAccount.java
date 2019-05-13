package com.example.edvin.app.models;

import com.google.gson.annotations.SerializedName;

public class UserAccount {
    private Integer id;
    //TODO : CHECK IF IT GENERATED AUTOMATICALLY

    private String password;

    @SerializedName("body")
    private String text;

    public UserAccount(String password) {
        this.password = password;
    }

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
