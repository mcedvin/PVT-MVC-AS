package com.example.edvin.app.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.edvin.app.R;
import com.example.edvin.app.logininterface.MainActivity;
import com.example.edvin.app.overview.OverviewActivity;

public class SettingsActivity extends AppCompatActivity {

    ImageButton backButton;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences = getSharedPreferences("autoLogin",Context.MODE_PRIVATE);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(l -> startActivity(new Intent(this, OverviewActivity.class)));


    }




    public void signOut(View view) {
        //Resetting value to 0 so autologin is disabled
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("key", 0);
        editor.remove("SerializableObject");
        editor.apply();

        Toast.makeText(this," Sign out successfully! ",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }
}
