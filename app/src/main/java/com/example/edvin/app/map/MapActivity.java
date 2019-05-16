package com.example.edvin.app.map;

import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.edvin.app.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private static final String TAG = "MapsActivity";
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 32;
    private LocationManager locationManager;
    private String provider;
    private Location currentLocation;
    private boolean locationUpdatesEnabled;
    private Map<Marker, String> markers = new HashMap<>();
    private Marker lastClicked = null;
    private final LatLngBounds STOCKHOLM_BOUNDS = new LatLngBounds(
            new LatLng(59.238131, 17.81566), new LatLng(59.408737, 18.325152));
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        setUpMapUI(googleMap);

        addMarkers();

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //show station info based on the marker clicked

                if (lastClicked == null) {
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.selectedstation_marker));
                    lastClicked = marker;

                } else {
                    //restore to default icon
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.defaultstation_marker));
                    lastClicked = null;
                }


                String stationId = markers.get(marker);

                //false = default behavior should occur in addition to custom behavior
                return false;
            }
        });
    }

    private void setUpMapUI(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
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
        mapUI.setCompassEnabled(true);
        mapUI.setZoomControlsEnabled(true);
        map.setPadding(0, 200, 0, 200);

    }


    private void addMarkers() {
    }
}
