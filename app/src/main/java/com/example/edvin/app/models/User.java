package com.example.edvin.app.models;

import com.google.gson.annotations.SerializedName;

public class User {

    private String firstName;
    private String lastName;
    private String email;
    private UserAccount userAccount;

    @SerializedName("body")
    private String text;

    public User(String firstName, String lastName, String email, String password, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

        this.userAccount = new UserAccount(id,password);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public String getText() {
        return text;
    }
}
