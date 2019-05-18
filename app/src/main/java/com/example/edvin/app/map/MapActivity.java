package com.example.edvin.app.map;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edvin.app.R;
import com.example.edvin.app.guide.GuideMainActivity;
import com.example.edvin.app.mainpage.HomePageDesign;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
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

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap map;
    private static final String TAG = "MapActivity";
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 32;
    private static final int PERMISSION_REQUEST_ENABLE_GPS = 33;
    private static final LatLngBounds STOCKHOLM_BOUNDS = new LatLngBounds(
            new LatLng(59.238131, 17.81566), new LatLng(59.408737, 18.325152));
    private static final float DEFAULT_ZOOM = 13;
    private final int[] START_MAP_PADDING = new int[]{0, 200, 0, 200};
    private FusedLocationProviderClient fusedLocationClient;
    private LocationManager locationManager;
    private String provider;
    private Location currentLocation;
    private Map<Marker, String> markers = new HashMap<>();
    private Marker lastClicked = null;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private boolean locationPermissionIsGranted;
    private BottomNavigationView bottomNavigationView;
    private SearchView searchView;
    private Spinner spinner;
    private TextView filterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        searchView = (SearchView) findViewById(R.id.searchView);
        spinner = (Spinner) findViewById(R.id.spinner);
        filterTextView = (TextView) findViewById(R.id.filterTextView);

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
                    default:
                        break;
                }
                return true;
            }
        });

    }

    private void goToHomeScreen() {
        Intent intent = new Intent(getApplicationContext(), HomePageDesign.class);
        startActivity(intent);
    }

    private void goToGuide() {
        Intent intent = new Intent(getApplicationContext(), GuideMainActivity.class);
        startActivity(intent);
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

        if (mapServicesEnabled()) {
            if (locationPermissionIsGranted) {
                enableLocationFunctionality();
            } else {
                getLocationPermission();
            }
        }

    }

    private void addMarkers() {
        // Add a marker in Sthlm
        LatLng sthlm = new LatLng(59.334591, 18.063240);
        Marker sthlmMarker = map.addMarker(new MarkerOptions().position(sthlm).title("Stockholm").snippet("").icon(BitmapDescriptorFactory.fromResource(R.drawable.defaultstation_marker)));
        markers.put(sthlmMarker, "STHLM");
        sthlmMarker.hideInfoWindow();


        // use marker.setVisible(boolean) to hide/show markers when filtering stations,
        // faster than creating/deleting markers

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
        mapUI.setCompassEnabled(true);
        mapUI.setZoomControlsEnabled(true);

        //makes sure Google logo, My Location button & zoom controls are not hidden behind UI widgets

        map.setPadding(START_MAP_PADDING[0], START_MAP_PADDING[1], START_MAP_PADDING[2], START_MAP_PADDING[3]);


    }

    private boolean mapServicesEnabled() {
        if (locationIsEnabled()) {
            return true;
        }
        return false;
    }

    public boolean locationIsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showMessageNoLocation();
            return false;
        }
        return true;
    }

    private void showMessageNoLocation() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.info_message_please_enable_gps)
                .setCancelable(false)
                .setPositiveButton(R.string.positive_option, new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSION_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSION_REQUEST_ENABLE_GPS: {
                if (locationPermissionIsGranted) {
                    enableLocationFunctionality();
                } else {
                    getLocationPermission();
                }
            }
        }
    }

    private void enableLocationFunctionality() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.setOnMyLocationButtonClickListener(this);
            map.setOnMyLocationClickListener(this);

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.d(TAG, "onSuccess: called.");
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                currentLocation = location;
                            } else {
                                Log.d(TAG, "location is null");
                            }
                            adjustZoom();
                        }
                    });
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        locationPermissionIsGranted = false;
        switch (requestCode) {
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                if (userGrantsPermission(grantResults)) {
                    enableLocationFunctionality();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.warning_message_user_denied_location_request, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private boolean userGrantsPermission(int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        return (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED);
    }


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by
         * onRequestPermissionsResult.
         */
        if (appHasLocationPermission()) {
            locationPermissionIsGranted = true;
            enableLocationFunctionality();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private boolean appHasLocationPermission() {
        return (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);

    }


    private void adjustZoom() {

        if (currentLocation != null) {

            //zoom to current location

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM));

        } else {

            //current location could not be found, adjust zoom to default view of Stockholm

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(STOCKHOLM_BOUNDS, width, height, padding);

            map.moveCamera(cu);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.stationMenuItem);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, R.string.on_my_location_click, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, R.string.on_my_location_button_click, Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }
}

