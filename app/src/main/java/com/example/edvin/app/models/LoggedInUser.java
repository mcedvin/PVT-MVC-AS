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

    private Collection<ChallengeAccepted> currentChallenges;
    private Collection<ChallengeAccepted> completedChallenges;



    public LoggedInUser(String displayName, int id ,Collection<ChallengeAccepted> currentChallenges,
                        Collection<ChallengeAccepted> completedChallenges) {
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

        if(currentChallenges.size()==0)
            return false;

        for (ChallengeAccepted ca: currentChallenges){
            if(ca.getChallenge().isEqual(ch))
                return true;
        }
        return false;
}

public boolean hasCompleted(Challenge ch){
    if(completedChallenges.size()==0)
        return false;

    for (ChallengeAccepted ca: completedChallenges){
        if(ca.getChallenge().isEqual(ch))
            return true;
    }
    return false;
}


}
