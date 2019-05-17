package com.example.edvin.app.mainpage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.edvin.app.R;
import com.example.edvin.app.guide.GuideMainActivity;
import com.example.edvin.app.map.MapActivity;

public class HomePageDesign extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_homepage2);
        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer);
        mToggle=new ActionBarDrawerToggle(HomePageDesign.this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView=(NavigationView)findViewById(R.id.NavigationView);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_settings_black_24dp);

        BottomNavigationView bottomNav = findViewById(R.id.navv_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.guideMenuItem:
                            homeToGuide();
                            break;
                        case R.id.stationMenuItem:
                            goToMap();
                            break;
                            default:
                                break;

                    }
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                      //      selectedFragment).commit();

                    return true;
                }
            };



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


    private void homeToGuide(){

        Intent mintent = new Intent(this, GuideMainActivity.class);
        startActivity(mintent);
    }

    private void goToMap(){

        Intent mintent = new Intent(this, MapActivity.class);
        startActivity(mintent);
    }
}
