package com.example.edvin.app.challenges;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.edvin.app.R;
import com.example.edvin.app.models.LoggedInUser;

public class AnimalsActivity extends AppCompatActivity {

    ImageButton backButton;
    Button buyLocalChallengeButton;
    LoggedInUser loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals);
        //loggedInUser = (LoggedInUser) getIntent().getExtras().getSerializable(getString(R.string.INTENT_KEY_USER));


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
}
