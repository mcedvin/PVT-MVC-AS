package com.example.edvin.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;

public class UserAccount {
    private Integer id;
    //TODO : CHECK IF IT GENERATED AUTOMATICALLY

    private String password;

    //private byte[] profilePicture;

    private Collection<ChallengeAccepted> currentChallenges;
    private Collection<ChallengeAccepted> completedChallenges;





    @SerializedName("body")
    private String text;

    public UserAccount(int id,String password) {     //byte[] profilePicture
        this.id= id;

        this.password = password;

        //this.profilePicture=profilePicture;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public Collection<ChallengeAccepted> getCurrentChallenges() {
        return currentChallenges;
    }

    public Collection<ChallengeAccepted> getCompletedChallenges() {
        return completedChallenges;
    }

    //public byte[] getProfilePicture() {
      //  return profilePicture;
    //}
    public String getText() {
        return text;
    }


}
