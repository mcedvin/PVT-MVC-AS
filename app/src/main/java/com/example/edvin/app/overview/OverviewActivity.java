package com.example.edvin.app.overview;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.edvin.app.R;
import com.example.edvin.app.challenges.ChallengesActivity;
import com.example.edvin.app.guide.GuideMainActivity;
import com.example.edvin.app.map.MapActivity;

public class OverviewActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        Button joinNewChallenge = findViewById(R.id.joinNewChallenge);
        joinNewChallenge.setOnClickListener(l -> startActivity(new Intent(this, ChallengesActivity.class)));


    }
}
