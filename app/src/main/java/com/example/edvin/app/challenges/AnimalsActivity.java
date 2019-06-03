package com.example.edvin.app.challenges;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.edvin.app.R;
import com.example.edvin.app.models.LoggedInUser;
import com.facebook.share.Share;
import com.google.gson.Gson;

public class AnimalsActivity extends AppCompatActivity {

    ImageButton backButton;
    Button buyLocalChallengeButton;
    LoggedInUser loggedInUser;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals);
        getTheUser();
        System.out.println(loggedInUser.getId());

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(l -> startActivity(new Intent(this, ChallengesCategoryScreenActivity.class)));

        buyLocalChallengeButton = findViewById(R.id.buyLocalChallengeButton);
        buyLocalChallengeButton.setOnClickListener(l -> goToBuyLocal());
    }

    private void goToBuyLocal() {

        Intent mintent = new Intent(this, BuyLocalChallengeActivity.class);
        mintent.putExtra(getString(R.string.INTENT_KEY_USER),loggedInUser);
        startActivity(mintent);
    }


    public void getTheUser(){
        sharedpreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        int j = sharedpreferences.getInt("key", 0);

        Gson gson = new Gson();
        String json = sharedpreferences.getString("SerializableObject", "");
        loggedInUser = gson.fromJson(json, LoggedInUser.class);
        System.out.println(loggedInUser.getId());
    }
}
