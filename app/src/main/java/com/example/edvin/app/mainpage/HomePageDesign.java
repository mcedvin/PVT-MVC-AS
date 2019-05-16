package com.example.edvin.app.mainpage;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.edvin.app.R;

public class HomePageDesign extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer);
        mToggle=new ActionBarDrawerToggle(HomePageDesign.this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView=(NavigationView)findViewById(R.id.NavigationView);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_settings_black_24dp);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if(id ==  R.id.change_password){

            Toast.makeText(this, "Detta är Ändra lösenord", Toast.LENGTH_SHORT).show();
        }
        if(id == R.id.delete_account){

            Toast.makeText(this, "Detta är Radera konto", Toast.LENGTH_SHORT).show();
        }
        if( id == R.id.Log_out){

            Toast.makeText(this, "Detta är Logga ut", Toast.LENGTH_SHORT).show();
        }
        if(id == R.id.privacy){

            Toast.makeText(this, "Detta är Sekretess och användarinställningar", Toast.LENGTH_SHORT).show();
        }


        return false;
    }
}
