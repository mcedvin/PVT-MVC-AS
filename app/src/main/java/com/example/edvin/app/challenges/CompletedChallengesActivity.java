package com.example.edvin.app.challenges;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.edvin.app.R;
import com.example.edvin.app.models.LoggedInUser;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;

public class CompletedChallengesActivity extends AppCompatActivity {

    Button NoPlast;
    String challenge1= "NoPlast";
    SharedPreferences sharedpreferences;
    LoggedInUser loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_challenges);

        NoPlast = findViewById(R.id.NoPlasticChallengeButton);
        NoPlast.setVisibility(View.INVISIBLE);

        getTheUser();

        setTheButton();

    }

    public void getTheUser(){
        sharedpreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        int j = sharedpreferences.getInt("key", 0);

        Gson gson = new Gson();
        String json = sharedpreferences.getString("SerializableObject", "");
        loggedInUser = gson.fromJson(json, LoggedInUser.class);
        System.out.println(loggedInUser.getId());
    }


    public void setTheButton(){
        if(loggedInUser.getCompletedChallenges().contains(challenge1)) {
            NoPlast.setVisibility(View.VISIBLE);
            NoPlast.setOnClickListener(l -> startActivity(new Intent(this, NoPlasticChallengeActivity.class)));
        }else
        NoPlast.setOnClickListener(null);
    }


}
