package com.example.edvin.app.map;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.edvin.app.R;
import com.example.edvin.app.guide.GuideMainActivity;
import com.example.edvin.app.models.LoggedInUser;
import com.example.edvin.app.models.Station;
import com.example.edvin.app.overview.OverviewActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.ArrayList;

public class StationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LoggedInUser loggedInUser;
    private GoogleMap map;
    private double cameraCenterLatitude, cameraCenterLongitude;
    private float cameraZoomLevel;
    private int DEFAULT_ZOOM = 13;
    private AutoCompleteTextView searchView;
    private BottomNavigationView bottomNavigationView;
    private TextView filterTextView;
    private ImageButton filterButton;
    private final String TAG = "StationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        getMap();

        ImageButton close = (ImageButton) findViewById(R.id.closeStationInfo);

        TextView stationName = (TextView) findViewById(R.id.stationName);

        Intent intent = getIntent();
        Station station = (Station) intent.getSerializableExtra(getString(R.string.INTENT_KEY_STATION));
        loggedInUser = (LoggedInUser) intent.getSerializableExtra(getString(R.string.INTENT_KEY_USER));
        Bundle fromReferringActivity = intent.getExtras();
        cameraCenterLatitude = fromReferringActivity.getDouble(getString(R.string.INTENT_KEY_CAMERA_LAT));
        cameraCenterLongitude = fromReferringActivity.getDouble(getString(R.string.INTENT_KEY_CAMERA_LONG));
        cameraZoomLevel = fromReferringActivity.getFloat(getString(R.string.INTENT_KEY_CAMERA_ZOOM));

        stationName.setText(station.getStationName());

        setUpBottomNavigationView();

    }

    private void setUpBottomNavigationView() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navv_view);
        bottomNavigationView.setSelectedItemId(R.id.stationMenuItem);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.homeMenuItem:
                        goToHomeScreen();
                        break;
                    case R.id.guideMenuItem:
                        goToGuide();
                        break;
                    case R.id.stationMenuItem:
                        goToMap();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }


    private void goToHomeScreen() {
        Intent homeIntent = new Intent(getApplicationContext(), OverviewActivity.class);
        homeIntent.putExtra(getString(R.string.INTENT_KEY_USER), loggedInUser);
        startActivity(homeIntent);
    }

    private void goToGuide() {
        Intent guideIntent = new Intent(getApplicationContext(), GuideMainActivity.class);
        guideIntent.putExtra(getString(R.string.INTENT_KEY_USER), loggedInUser);
        startActivity(guideIntent);
    }

    private void goToMap() {
        Intent mapIntent = new Intent(this, MapActivity.class);
        Bundle bundle = new Bundle();
        mapIntent.putExtra(getString(R.string.INTENT_KEY_USER), loggedInUser);
        bundle.putDouble(getString(R.string.INTENT_KEY_CAMERA_LAT), map.getCameraPosition().target.latitude);
        bundle.putDouble(getString(R.string.INTENT_KEY_CAMERA_LONG), map.getCameraPosition().target.longitude);
        bundle.putFloat(getString(R.string.INTENT_KEY_CAMERA_ZOOM), map.getCameraPosition().zoom);
        mapIntent.putExtras(bundle);
        startActivity(mapIntent);
    }

    private void getMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapStationActivity);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cameraCenterLatitude, cameraCenterLongitude), cameraZoomLevel));

        setUpMapUI(map);

    }


    private void setUpMapUI(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined in a raw resource file.

            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style template. Error: ", e);
        }

        UiSettings mapUI = googleMap.getUiSettings();

    }
}
