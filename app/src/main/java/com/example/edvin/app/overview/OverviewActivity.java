package com.example.edvin.app.overview;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.edvin.app.R;
import com.example.edvin.app.challenges.ChallengesActivity;
import com.example.edvin.app.guide.GuideMainActivity;
import com.example.edvin.app.map.MapActivity;
import com.example.edvin.app.settings.SettingsActivity;

public class OverviewActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    ImageButton settingsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(l -> startActivity(new Intent(this, SettingsActivity.class)));

        Button joinNewChallenge = findViewById(R.id.joinNewChallenge);
        joinNewChallenge.setOnClickListener(l -> startActivity(new Intent(this, ChallengesActivity.class)));


        //bottomNav = findViewById(R.id.navv_view); ********** FEL NAMN!!!!!************
        bottomNav = findViewById(R.id.bottomNavBar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    private void homeToGuide() {

        Intent mintent = new Intent(this, GuideMainActivity.class);
        startActivity(mintent);
    }

    private void goToMap() {

        Intent mintent = new Intent(this, MapActivity.class);
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

}