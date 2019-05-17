package com.example.edvin.app.logininterface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.edvin.app.R;

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
