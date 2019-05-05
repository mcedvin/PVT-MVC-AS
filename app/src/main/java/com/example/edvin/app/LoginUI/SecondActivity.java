package com.example.edvin.app.LoginUI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.edvin.app.LoginData.LoginBackground;
import com.example.edvin.app.R;

public class SecondActivity extends AppCompatActivity {

    EditText passTxt;
    EditText userTxt;
    Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        passTxt = findViewById(R.id.passTxt);
        userTxt = findViewById(R.id.userTxt);
        startBtn = findViewById(R.id.startBtn);

    }


    public void onClick(View view){



        String type = "login";

        String username = userTxt.getText().toString();
        String password = passTxt.getText().toString();




        LoginBackground bgw = new LoginBackground(this);
        bgw.execute(type,username,password);



    }
}