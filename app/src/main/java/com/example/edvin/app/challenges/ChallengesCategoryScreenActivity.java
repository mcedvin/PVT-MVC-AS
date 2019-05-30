package com.example.edvin.app.challenges;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.edvin.app.R;
import com.example.edvin.app.overview.OverviewActivity;

public class ChallengesCategoryScreenActivity extends AppCompatActivity {

    Button oceanButton;
    Button natureButton;
    Button animalsButton;
    Button airButton;
    ImageButton backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges_category_screen);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(l -> startActivity(new Intent(this, OverviewActivity.class)));

        oceanButton = findViewById(R.id.oceanButton);
        oceanButton.setOnClickListener(l -> startActivity(new Intent(this, OceanActivity.class)));

        natureButton = findViewById(R.id.natureButton);
        natureButton.setOnClickListener(l -> startActivity(new Intent(this, NatureActivity.class)));

        animalsButton = findViewById(R.id.animalsButton);
        animalsButton.setOnClickListener(l -> startActivity(new Intent(this, AnimalsActivity.class)));

        airButton = findViewById(R.id.airButton);
        airButton.setOnClickListener(l -> startActivity(new Intent(this, AirActivity.class)));

    }
}

