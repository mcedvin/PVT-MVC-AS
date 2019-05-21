package com.example.edvin.app.guide;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.edvin.app.R;
import com.example.edvin.app.map.MapActivity;
import com.example.edvin.app.models.LoggedInUser;
import com.example.edvin.app.overview.OverviewActivity;

public class GuideMainActivity extends AppCompatActivity {

    LoggedInUser loggedInUser;
    private Button metall;
    private Button glas;
    private Button tidningar;
    private Button batterier;
    private Button farligtavfall;
    private Button glodlampor;
    private Button forpackningar;
    private Button matavfall;
    private Button elektronik;
    private Button medicin;
    private Button plast;
    private Button papper;
    private Button tradgard;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_main);
        /**
         * info from användare
         */
        loggedInUser = (LoggedInUser) getIntent().getExtras().getSerializable("serialize_data");



        bottomNav = findViewById(R.id.navv_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        metall = (Button) findViewById(R.id.metall);
        metall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMetallActivity();
            }
        });

        glas = (Button) findViewById(R.id.glas);
        glas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGlasActivity();
            }
        });

        tidningar = (Button) findViewById(R.id.tidningar);
        tidningar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTidningarActivity();
            }
        });

        batterier = (Button) findViewById(R.id.batterier);
        batterier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBatterierActivity();
            }
        });

        farligtavfall = (Button) findViewById(R.id.farligt_Avfall);
        farligtavfall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFarligtavfallActivity();
            }
        });

        metall = (Button) findViewById(R.id.metall);
        metall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMetallActivity();
            }
        });

        glodlampor = (Button) findViewById(R.id.glodlampor);
        glodlampor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGlodlamporlActivity();
            }
        });

        forpackningar = (Button) findViewById(R.id.forpackningar);
        forpackningar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForpackningarActivity();
            }
        });

        matavfall = (Button) findViewById(R.id.matavfall);
        matavfall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMatActivity();
            }
        });

        elektronik = (Button) findViewById(R.id.elektronik);
        elektronik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openElektronikActivity();
            }
        });

        medicin = (Button) findViewById(R.id.medicin);
        medicin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMedicinActivity();
            }
        });

        plast = (Button) findViewById(R.id.plast);
        plast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlastActivity();
            }
        });

        papper = (Button) findViewById(R.id.papper);
        papper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPapperActivity();
            }
        });

        tradgard = (Button) findViewById(R.id.tradgardsavfall);
        tradgard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTradgardActivity();
            }
        });



    }
    public void openMetallActivity(){
        Intent intent = new Intent(this, MetalActivity.class);
        startActivity(intent);
    }

    public void openGlasActivity(){
        Intent intent = new Intent(this, GlasActivity.class);
        startActivity(intent);
    }

    public void openTidningarActivity(){
        Intent intent = new Intent(this, TidningarActivity.class);
        startActivity(intent);
    }

    public void openBatterierActivity(){
        Intent intent = new Intent(this, BatterierActivity.class);
        startActivity(intent);
    }

    public void openFarligtavfallActivity(){
        Intent intent = new Intent(this, FarligtavfallActivity.class);
        startActivity(intent);
    }

    public void openGlodlamporlActivity(){
        Intent intent = new Intent(this, GlodlamporActivity.class);
        startActivity(intent);
    }

    public void openForpackningarActivity(){
        Intent intent = new Intent(this, ForpackningarActivity.class);
        startActivity(intent);
    }

    public void openMatActivity(){
        Intent intent = new Intent(this, MatActivity.class);
        startActivity(intent);
    }

    public void openElektronikActivity(){
        Intent intent = new Intent(this, ElektronikActivity.class);
        startActivity(intent);
    }

    public void openMedicinActivity(){
        Intent intent = new Intent(this, MedicinActivity.class);
        startActivity(intent);
    }

    public void openPlastActivity(){
        Intent intent = new Intent(this, PlastActivity.class);
        startActivity(intent);
    }

    public void openPapperActivity(){
        Intent intent = new Intent(this, PapperActivity.class);
        startActivity(intent);
    }

    public void openTradgardActivity(){
        Intent intent = new Intent(this, TradgardActivity.class);
        startActivity(intent);
    }


    private void goToMap(){

        Intent mintent = new Intent(this, MapActivity.class);
        mintent.putExtra("serialize_data",loggedInUser);
        startActivity(mintent);
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    //Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.homeMenuItem:
                            guideToHome();
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



    private void guideToHome(){

        Intent mintent = new Intent(this, OverviewActivity.class);
        mintent.putExtra("serialize_data",loggedInUser);
        startActivity(mintent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.guideMenuItem);
    }

}
