package com.example.edvin.app.challenges;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.edvin.app.R;
import com.example.edvin.app.models.LoggedInUser;
import com.example.edvin.app.overview.OverviewActivity;
import com.example.edvin.app.settings.SettingsActivity;

public class ChallengesActivity extends AppCompatActivity {

    ImageButton backButton;
    Button reduceButton;
    Button reuseButton;
    Button recycleButton;
    ImageButton challengeOfTheMonthButton;
    LoggedInUser loggedInUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        loggedInUser = (LoggedInUser) getIntent().getExtras().getSerializable(getString(R.string.INTENT_KEY_USER));

        backButton = findViewById(R.id.backButton);
        reduceButton = findViewById(R.id.reduceButton);
        reuseButton = findViewById(R.id.reuseButton);
        recycleButton = findViewById(R.id.recycleButton);
        challengeOfTheMonthButton = findViewById(R.id.challengeOfTheMonthButton);

        backButton.setOnClickListener(l -> challengeToHome());
        challengeOfTheMonthButton.setOnClickListener(l -> startActivity(new Intent(this, ChallengeInformationActivity.class)));

        CategoryListener categoryListener = new CategoryListener();

        reduceButton.setOnClickListener(categoryListener);
        reuseButton.setOnClickListener(categoryListener);
        recycleButton.setOnClickListener(categoryListener);
        reduceButton.performClick();


    }

    private void challengeToHome(){

        Intent mintent = new Intent(this, OverviewActivity.class);
        mintent.putExtra("serialize_data",loggedInUser);
        startActivity(mintent);
    }


    class CategoryListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.equals(reduceButton)) {
                reduceButton.setBackgroundColor(getResources().getColor(R.color.coralColor));

                reuseButton.setBackgroundColor(getResources().getColor(R.color.lightGrey));
                recycleButton.setBackgroundColor(getResources().getColor(R.color.lightGrey));

            } else if (v.equals(reuseButton)) {
                reuseButton.setBackgroundColor(getResources().getColor(R.color.coralColor));

                reduceButton.setBackgroundColor(getResources().getColor(R.color.lightGrey));
                recycleButton.setBackgroundColor(getResources().getColor(R.color.lightGrey));

            } else {
                recycleButton.setBackgroundColor(getResources().getColor(R.color.coralColor));

                reduceButton.setBackgroundColor(getResources().getColor(R.color.lightGrey));
                reuseButton.setBackgroundColor(getResources().getColor(R.color.lightGrey));

            }

        }

    }
}

