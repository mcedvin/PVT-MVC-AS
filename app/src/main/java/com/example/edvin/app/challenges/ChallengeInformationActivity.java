package com.example.edvin.app.challenges;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.edvin.app.R;
import com.example.edvin.app.overview.OverviewActivity;

public class ChallengeInformationActivity extends AppCompatActivity {

    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_information);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(l -> startActivity(new Intent(this, ChallengesActivity.class)));


    }
}
