package com.example.edvin.app.challenges;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.edvin.app.R;
import com.example.edvin.app.map.MapActivity;
import com.example.edvin.app.models.LoggedInUser;
import com.example.edvin.app.overview.OverviewActivity;

public class ChallengesCategoryScreenActivity extends AppCompatActivity {

    Button oceanButton;
    Button natureButton;
    Button animalsButton;
    Button airButton;
    ImageButton backButton;
    LoggedInUser loggedInUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges_category_screen);
        //loggedInUser = (LoggedInUser) getIntent().getExtras().getSerializable(getString(R.string.INTENT_KEY_USER));


        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(l -> startActivity(new Intent(this, OverviewActivity.class)));

        oceanButton = findViewById(R.id.oceanButton);
        oceanButton.setOnClickListener(l -> startActivity(new Intent(this, OceanActivity.class)));

        natureButton = findViewById(R.id.natureButton);
        natureButton.setOnClickListener(l -> startActivity(new Intent(this, NatureActivity.class)));

        animalsButton = findViewById(R.id.animalsButton);
        animalsButton.setOnClickListener(l -> goToAnimal());

        airButton = findViewById(R.id.airButton);
        airButton.setOnClickListener(l -> startActivity(new Intent(this, AirActivity.class)));

    }

    private void goToAnimal() {

        Intent mintent = new Intent(this, AnimalsActivity.class);
        mintent.putExtra(getString(R.string.INTENT_KEY_USER),loggedInUser);
        startActivity(mintent);
    }
}

