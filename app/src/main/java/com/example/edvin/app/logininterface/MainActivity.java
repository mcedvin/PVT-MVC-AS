package com.example.edvin.app.logininterface;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.edvin.app.R;
import com.example.edvin.app.challenges.ChallengesActivity;
import com.example.edvin.app.models.LoggedInUser;
import com.example.edvin.app.overview.OverviewActivity;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    LoggedInUser loggedInUser;
    SharedPreferences sharedpreferences;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Loading...");
        mDialog.setTitle("Welcome!");
        mDialog.show();
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        int j = sharedpreferences.getInt("key", 0);

        //Default is 0 so autologin is disabled

        if(j > 0){
            mDialog.dismiss();
            Gson gson = new Gson();
            String json = sharedpreferences.getString("SerializableObject", "");
            loggedInUser = gson.fromJson(json, LoggedInUser.class);

            Intent activity = new Intent(getApplicationContext(), OverviewActivity.class);

            activity.putExtra(getString(R.string.INTENT_KEY_USER),loggedInUser);
            startActivity(activity);
        }
        mDialog.dismiss();




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
