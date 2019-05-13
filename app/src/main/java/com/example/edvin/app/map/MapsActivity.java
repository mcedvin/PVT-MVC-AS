package com.example.edvin.app.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.edvin.app.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 32;
    private LocationManager locationManager;
    private String provider;
    private Location currentLocation;
    private boolean locationUpdatesEnabled;
    private Map<Marker, String> mMarkers = new HashMap<>();
    private Marker lastClicked = null;
    private final LatLngBounds STOCKHOLM_BOUNDS = new LatLngBounds(
            new LatLng(59.238131, 17.81566), new LatLng(59.408737, 18.325152));
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    public MapsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        checkLocationPermission();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Logic for handling location update
                    currentLocation = location;
                }
            }

            ;
        };
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

        mMap = googleMap;

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

        addMarkers();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //show station info based on the marker clicked

                if (lastClicked == null) {
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.selectedstation));
                    lastClicked = marker;

                } else {
                    //restore to default icon
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.defaultstation));
                    lastClicked = null;
                }


                String stationId = mMarkers.get(marker);

                //false = default behavior should occur in addition to custom behavior
                return false;
            }
        });

        // Set the camera to the greatest possible zoom level that includes the
        // bounds
        adjustZoom();

        mMap.setPadding(0, 100, 0, 300);

    }

    private void adjustZoom() {

            //adjust zoom to default view of Stockholm, should be changed to actual current location

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(STOCKHOLM_BOUNDS, width, height, padding);

            mMap.moveCamera(cu);

//
    }

    private void addMarkers() {
        // Add a marker in Sthlm and move the camera
        LatLng sthlm = new LatLng(59.334591, 18.063240);
        Marker sthlmMarker = mMap.addMarker(new MarkerOptions().position(sthlm).title("Stockholm").snippet("").icon(BitmapDescriptorFactory.fromResource(R.drawable.defaultstation)));
        mMarkers.put(sthlmMarker, "STHLM");
        sthlmMarker.hideInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sthlm));


        // use marker.setVisible(boolean) to hide/show markers when filtering stations, faster than creating/deleting markers

    }


    private void checkLocationPermission() {
        if (appHasLocationPermission()) {
            enableLocationFunctionality();
        } else {
            requestLocationPermission();
        }
    }

    private boolean appHasLocationPermission() {
        return (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);

    }

    private void requestLocationPermission() {
        // PERMISSION_REQUEST_ACCESS_FINE_LOCATION is an
        // app-defined int constant. The callback method gets the
        // result of the request
        ActivityCompat.requestPermissions(MapsActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            if (userGrantsPermission(grantResults)) {
                enableLocationFunctionality();
            } else {
                // permission denied! Disable the
                // functionality that depends on this permission.
                Toast.makeText(getApplicationContext(), "Vissa av appens funktioner fungerar inte utan tillgång till din position", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void enableLocationFunctionality() {
        if (mMap != null) {
            try {
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(this);
                mMap.setOnMyLocationClickListener(this);
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object
                                    currentLocation = location;
                                }
                            }
                        });
                enableLocationUpdates(true);
            } catch (SecurityException e) {
                Toast.makeText(getApplicationContext(), "Ett fel uppstod när appen försökte på tillgång till din position", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean userGrantsPermission(int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        return (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED);
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(15000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Nuvarande position", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Nuvarande position", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        enableLocationUpdates(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableLocationUpdates(true);
    }


    private void enableLocationUpdates(boolean value) {
        locationUpdatesEnabled = value;
        if (locationUpdatesEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (appHasLocationPermission()) {
                    try {
                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    } catch (SecurityException e) {
                        Toast.makeText(getApplicationContext(), "Ett fel uppstod när appen försökte på tillgång till din position", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } else {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }

    }








}
