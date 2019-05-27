package com.example.edvin.app.challenges;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.edvin.app.R;

public class OceanActivity extends AppCompatActivity {

    ImageButton backButton;
    Button NoPlasticChallengeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocean);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(l -> startActivity(new Intent(this, ChallengesCategoryScreenActivity.class)));

        NoPlasticChallengeButton = findViewById(R.id.NoPlasticChallengeButton);
        NoPlasticChallengeButton.setOnClickListener(l -> startActivity(new Intent(this, NoPlasticChallengeActivity.class)));

    }
}
