package com.example.edvin.app.map;

import android.graphics.Color;
import android.graphics.Path;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.edvin.app.R;

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
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.edvin.app.guide.GuideMainActivity;
import com.example.edvin.app.models.LoggedInUser;
import com.example.edvin.app.models.Position;
import com.example.edvin.app.models.Station;
import com.example.edvin.app.overview.OverviewActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectionActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnPolylineClickListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private LoggedInUser loggedInUser;
    private GoogleMap map;
    private static final String TAG = "REGO: DirectionActivity";
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
    private Map<Marker, Station> markersAndStations = new HashMap<>();
    private Marker lastClicked = null;
    private boolean locationPermissionIsGranted;
    private BottomNavigationView bottomNavigationView;
    private Station station;
    private ArrayList<Station> stationList;
    private GeoApiContext geoApiContext;
    private Marker target;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        Log.d(TAG, "DirectionActivity launched!");

        getDataFromIntent();

        getLocationServices();

        setUpBottomNavigationView();

        getMap();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();

        station = (Station) intent.getSerializableExtra(getString(R.string.INTENT_KEY_STATION));
        stationList = (ArrayList<Station>) intent.getSerializableExtra(getString(R.string.INTENT_KEY_STATION_LIST));
        loggedInUser = (LoggedInUser) intent.getSerializableExtra(getString(R.string.INTENT_KEY_USER));
        currentLocation = (Location) intent.getParcelableExtra(getString(R.string.INTENT_KEY_USER_LOCATION));

    }


    private void setUpBottomNavigationView() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navv_view_direction);
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
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void goToMap() {
        Intent mapIntent = new Intent(this, MapActivity.class);
        startActivity(mapIntent);
    }

    private void getMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapDirectionActivity);
        mapFragment.getMapAsync(this);

        if (geoApiContext == null) {
            geoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_maps_key))
                    .build();
        }
    }

    private void getLocationServices() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(getApplicationContext(), OverviewActivity.class);
        intent.putExtra(getString(R.string.INTENT_KEY_USER), loggedInUser);
        startActivity(intent);
    }

    private void goToGuide() {
        Intent intent = new Intent(getApplicationContext(), GuideMainActivity.class);
        intent.putExtra(getString(R.string.INTENT_KEY_USER), loggedInUser);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */

        map = googleMap;

        setUpMapUI(googleMap);

        addMarkersOnMap();

        ensureAccessToLocation();

        map.setOnPolylineClickListener(this);

    }

    private void ensureAccessToLocation() {
        if (mapServicesEnabled()) {
            if (locationPermissionIsGranted) {
                enableLocationFunctionality();
            } else {
                getLocationPermission();
            }
        }
    }

    private void addMarkersOnMap() {

        for (Station s : stationList) {

            Position p = s.getPosition();
            LatLng latlong = new LatLng(p.getX(), p.getY());

            Marker marker = map.addMarker(new MarkerOptions().position(latlong).title(s.toString()).snippet("").icon(BitmapDescriptorFactory.fromResource(R.drawable.defaultstation_marker)));
            marker.hideInfoWindow();
            markersAndStations.put(marker, s);
            marker.setVisible(false);

            if (s.equals(station)) {
                target = marker;
                marker.setVisible(true);
                marker.showInfoWindow();
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.selectedstation_marker));
            }

        }

    }

    private void setUpMapUI(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined in a raw resource file.

            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing of map failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style template. Error: ", e);
        }

        UiSettings mapUI = googleMap.getUiSettings();
        mapUI.setCompassEnabled(true);
        mapUI.setZoomControlsEnabled(true);
        map.setOnMarkerClickListener(this);
        map.setOnInfoWindowClickListener(this);

        //makes sure Google logo, My Location button & zoom controls are not hidden behind UI widgets
        //@TODO params left, top, right, bottom should be calculated from screen size instead of hardcoded
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
            showMessageWhenLocationIsDisabled();
            return false;
        }
        return true;
    }

    private void showMessageWhenLocationIsDisabled() {
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
                            Log.d(TAG, "onSuccess() called, got last known location but might be null");
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                currentLocation = location;
                                calculateDirections(target);
                            } else {
                                Log.d(TAG, "location is null");
                            }
                            adjustCameraZoomOnMap();
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
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
    }

    private boolean appHasLocationPermission() {
        return (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);

    }

    private void adjustCameraZoomOnMap() {

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

    private void calculateDirections(Marker marker) {

        Log.d(TAG, "calculateDirections: Calculating directions");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                marker.getPosition().latitude,
                marker.getPosition().longitude
        );

        DirectionsApiRequest directions = new DirectionsApiRequest(geoApiContext);

        directions.alternatives(false);

        directions.origin(
                new com.google.maps.model.LatLng(
                        currentLocation.getLatitude(),
                        currentLocation.getLongitude()
                )
        );

        Log.d(TAG, "calculateDirections: destination: " + destination.toString());

        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "onResult: routes: " + result.routes[0].toString());
                Log.d(TAG, "onResult: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());

                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "onFailure: " + e.getMessage());

            }
        });
    }


    private void addPolylinesToMap(final DirectionsResult result) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                for (DirectionsRoute route : result.routes) {

                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for (com.google.maps.model.LatLng latLng : decodedPath) {

                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }
                    Polyline polyline = map.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(Color.BLUE);
                    polyline.setClickable(true);
                    zoomRoute(polyline.getPoints());

                }
            }
        });
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        zoomRoute(polyline.getPoints());
    }

    public void zoomRoute(List<LatLng> lstLatLngRoute) {

        if (map == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 60;

        LatLngBounds latLngBounds = boundsBuilder.build();

        map.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
                600,
                null
        );
    }


    @Override
    public void onInfoWindowClick(final Marker marker) {
        Log.d(TAG, "info window clicked!");
        openGoogleMapsDialog(marker);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "marker clicked!");
        if (marker.equals(target)) {
            openGoogleMapsDialog(marker);
        }
        return true;
    }

    private void openGoogleMapsDialog(Marker marker) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DirectionActivity.this);

        builder.setMessage("Öppna Google Maps?")
                .setCancelable(true)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {

                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                        String latitude = String.valueOf(marker.getPosition().latitude);
                        String longitude = String.valueOf(marker.getPosition().longitude);

                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);

                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                        mapIntent.setPackage("com.google.android.apps.maps");

                        try {
                            if (mapIntent.resolveActivity(DirectionActivity.this.getPackageManager()) != null) {
                                startActivity(mapIntent);
                            }
                        } catch (NullPointerException e) {
                            Log.e(TAG, "onClick: NullPointerException: Couldn't open map." + e.getMessage());
                            Toast.makeText(DirectionActivity.this, "Kunde inte öppna Google Maps", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Nej", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}






