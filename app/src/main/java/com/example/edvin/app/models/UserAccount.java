package com.example.edvin.app.models;

import com.google.gson.annotations.SerializedName;

public class UserAccount {
    private Integer id;
    //TODO : CHECK IF IT GENERATED AUTOMATICALLY

    private String password;

    @SerializedName("body")
    private String text;

    public UserAccount(int id,String password) {
        this.id= id;

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
