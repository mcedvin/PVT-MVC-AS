package com.example.edvin.app.models;

import android.media.Image;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.Collection;

/**
 * Class exposing authenticated user details to the UI.
 */

public class LoggedInUser implements Serializable {

    private String displayName;
    private int id;
    private Image avatar = null; //bytes ?

    private Collection<Challenge> currentChallenges;
    private Collection<Challenge> completedChallenges;



    public LoggedInUser(String displayName, int id ,Collection<Challenge> currentChallenges,
                        Collection<Challenge> completedChallenges) {
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


public boolean hasCurrent(Challenge ch){
        return currentChallenges.contains(ch);
}

public boolean hasCompleted(Challenge ch){
        return completedChallenges.contains(ch);
}


}
