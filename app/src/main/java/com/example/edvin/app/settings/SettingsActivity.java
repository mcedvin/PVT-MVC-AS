package com.example.edvin.app.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.edvin.app.R;
import com.example.edvin.app.models.LoggedInUser;
import com.example.edvin.app.overview.OverviewActivity;

public class SettingsActivity extends AppCompatActivity {

    ImageButton backButton;
    LoggedInUser loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /**
         * info from användare
         */
        loggedInUser = (LoggedInUser) getIntent().getExtras().getSerializable("serialize_data");

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(l ->settingToHome());


    }


    private void settingToHome(){

        Intent mintent = new Intent(this, OverviewActivity.class);
        mintent.putExtra("serialize_data",loggedInUser);
        startActivity(mintent);
    }
}
