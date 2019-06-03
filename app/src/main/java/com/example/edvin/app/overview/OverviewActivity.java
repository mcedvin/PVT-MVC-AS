package com.example.edvin.app.overview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.edvin.app.challenges.CompletedChallengesActivity;
import com.example.edvin.app.R;
import com.example.edvin.app.challenges.ChallengesCategoryScreenActivity;
import com.example.edvin.app.challenges.NoPlasticChallengeActivity;
import com.example.edvin.app.guide.GuideMainActivity;
import com.example.edvin.app.map.MapActivity;
import com.example.edvin.app.models.LoggedInUser;
import com.example.edvin.app.settings.SettingsActivity;
import com.google.gson.Gson;

public class OverviewActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    ImageButton settingsButton;
    LoggedInUser loggedInUser;
    Button showAllFinishedChallenges;
    SharedPreferences sharedpreferences;
    Button NoPlast;
    String challenge1= "NoPlast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        NoPlast = findViewById(R.id.NoPlasticChallengeButton);
        NoPlast.setVisibility(View.INVISIBLE);


        /**
         * info from användare
         */
        //TODO: LOAD ALWAYS LIKE THIS IN ALL THE CLASSES
        //loggedInUser = (LoggedInUser) getIntent().getExtras().getSerializable("user");
        getTheUser();
        setTheButton();
        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(l -> homeToSetting());

        Button joinNewChallenge = findViewById(R.id.joinNewChallenge);
        joinNewChallenge.setOnClickListener(l -> homeToNewChallenge());

        showAllFinishedChallenges = findViewById(R.id.showAllFinishedChallenges);
        showAllFinishedChallenges.setOnClickListener(l -> startActivity(new Intent(this, CompletedChallengesActivity.class)));


        //bottomNav = findViewById(R.id.navv_view); ********** FEL NAMN!!!!!************
        bottomNav = findViewById(R.id.bottomNavBar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    private void homeToGuide() {

        Intent mintent = new Intent(this, GuideMainActivity.class);
        mintent.putExtra(getString(R.string.INTENT_KEY_USER),loggedInUser);
        startActivity(mintent);
    }

    private void homeToSetting() {

        Intent mintent = new Intent(this, SettingsActivity.class);
        mintent.putExtra(getString(R.string.INTENT_KEY_USER),loggedInUser);
        startActivity(mintent);
    }

    private void homeToNewChallenge() {

        Intent mintent = new Intent(this, ChallengesCategoryScreenActivity.class);
        mintent.putExtra(getString(R.string.INTENT_KEY_USER),loggedInUser);
        startActivity(mintent);
    }

    private void goToMap() {

        Intent mintent = new Intent(this, MapActivity.class);
        mintent.putExtra(getString(R.string.INTENT_KEY_USER),loggedInUser);
        startActivity(mintent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.homeMenuItem);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    //Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.guideMenuItem:
                            homeToGuide();
                            break;
                        case R.id.stationMenuItem:
                            goToMap();
                            break;
                        default:
                            break;

                    }


                    return true;
                }
    };

    public void getTheUser(){
        sharedpreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        int j = sharedpreferences.getInt("key", 0);

        Gson gson = new Gson();
        String json = sharedpreferences.getString("SerializableObject", "");
        loggedInUser = gson.fromJson(json, LoggedInUser.class);
        System.out.println(loggedInUser.getId());
    }

    public void setTheButton(){
        if(loggedInUser.getCurrentChallenges().contains(challenge1)) {
            NoPlast.setVisibility(View.VISIBLE);
            NoPlast.setOnClickListener(l -> startActivity(new Intent(this, NoPlasticChallengeActivity.class)));
        }else
            NoPlast.setOnClickListener(null);

    }

}