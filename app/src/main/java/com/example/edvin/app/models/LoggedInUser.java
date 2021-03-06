package com.example.edvin.app.models;

import android.media.Image;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Class exposing authenticated user details to the UI.
 */

public class LoggedInUser implements Serializable {

    private String displayName;
    private int id;
    private Image avatar = null; //bytes ?

    private ArrayList<String> currentChallenges;
    private ArrayList<String> completedChallenges;

    public ArrayList<String> getCurrentChallenges() {
        return currentChallenges;
    }

    public ArrayList<String> getCompletedChallenges() {
        return completedChallenges;
    }

    //TODO: CHANGE CHALLENGES TO STRINGS


    public LoggedInUser(String displayName, int id , ArrayList<String> currentChallenges,
                        ArrayList<String> completedChallenges) {
        this.displayName = displayName;
        this.id = id;

        this.completedChallenges=completedChallenges;
        this.currentChallenges = currentChallenges;


        //default image?
    }

    String getDisplayName() {
        return displayName;
    }

    public int getId() {
        return id;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


public boolean hasCurrent(String challengeName){

        if(currentChallenges.size()==0)
            return false;

        for (String ca: currentChallenges){
            if(ca.equals(challengeName))
                return true;
        }
        return false;
}

public boolean hasCompleted(String challengeName){
    if(completedChallenges.size()==0)
        return false;

    for (String ca: completedChallenges){
        if(ca.equals(challengeName))
            return true;
    }
    return false;
}


}
