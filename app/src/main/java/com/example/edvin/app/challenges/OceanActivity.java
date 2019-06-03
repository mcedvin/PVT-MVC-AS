package com.example.edvin.app.challenges;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.edvin.app.R;
import com.example.edvin.app.models.LoggedInUser;
import com.google.gson.Gson;

public class OceanActivity extends AppCompatActivity {

    ImageButton backButton;
    Button NoPlasticChallengeButton;
    SharedPreferences sharedpreferences;
    LoggedInUser loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocean);
        getTheUser();
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(l -> startActivity(new Intent(this, ChallengesCategoryScreenActivity.class)));

        NoPlasticChallengeButton = findViewById(R.id.NoPlasticChallengeButton);
        NoPlasticChallengeButton.setOnClickListener(l -> startActivity(new Intent(this, NoPlasticChallengeActivity.class)));

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
