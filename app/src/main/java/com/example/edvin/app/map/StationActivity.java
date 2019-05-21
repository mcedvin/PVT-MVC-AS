package com.example.edvin.app.map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.edvin.app.R;
import com.example.edvin.app.models.LoggedInUser;
import com.example.edvin.app.models.Station;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class StationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LoggedInUser loggedInUser;
    private GoogleMap map;
    private double cameraCenterLatitude, cameraCenterLongitude;
    private int DEFAULT_ZOOM = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapStationActivity);
        mapFragment.getMapAsync(this);
        ImageButton close = (ImageButton)findViewById(R.id.closeStationInfo);

        TextView stationName = (TextView)findViewById(R.id.stationName);


        Intent intent = getIntent();
        Station station =(Station)intent.getSerializableExtra("station");
        loggedInUser = (LoggedInUser) intent.getSerializableExtra("user");
        Bundle fromReferringActivity = intent.getExtras();
        double cameraCenterLatitude = fromReferringActivity.getDouble("camera center latitude");
        double cameraCenterLongitude = fromReferringActivity.getDouble("camera center longitude");

        stationName.setText(station.getStationName());


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cameraCenterLatitude, cameraCenterLongitude), DEFAULT_ZOOM));

    }
}
