package com.example.edvin.app.logininterface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.edvin.app.R;
import com.example.edvin.app.challenges.ChallengesActivity;
import com.example.edvin.app.mainpage.HomePageDesign;
import com.example.edvin.app.overview.OverviewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void onStart(View view){
        Intent mintent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(mintent);
    }

    public void onStart2(View view){
        Intent mintent = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(mintent);
    }


}
