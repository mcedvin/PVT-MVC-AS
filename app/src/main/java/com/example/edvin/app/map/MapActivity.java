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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edvin.app.R;
import com.example.edvin.app.guide.GuideMainActivity;
import com.example.edvin.app.models.LoggedInUser;
import com.example.edvin.app.models.Material;
import com.example.edvin.app.models.Position;
import com.example.edvin.app.models.Station;
import com.example.edvin.app.overview.OverviewActivity;
import com.example.edvin.app.util.BaseApiService;
import com.example.edvin.app.util.RetrofitClient;
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
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener {

    LoggedInUser loggedInUser;
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
    private Map<Marker, Station> markersAndStations = new HashMap<>();
    private Marker lastClicked = null;
    private boolean locationPermissionIsGranted;
    private BottomNavigationView bottomNavigationView;
    private AutoCompleteTextView searchView;
    private TextView filterTextView;
    private ImageButton filterButton;
    private AlertDialog dialog = null;
    private boolean selectAll = true;
    private int noOfFilterItems;
    private boolean[] checkedFilterOptions;
    private String[] filterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        /**
         * info from användare
         * loggedInUser = (LoggedInUser) getIntent().getExtras().getSerializable("serialize_data");
         */

        getMap();

        getLocationServices();

        setUpSearchBar();

        setUpFilterWidgets();

        setUpBottomNavigationView();

    }

    private void setUpSearchBar() {
        searchView = (AutoCompleteTextView) findViewById(R.id.searchView);
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Station s = (Station) parent.getItemAtPosition(position);
                goToStation(s);
            }
        });
    }

    private void setUpAutoCompleteSearch() {
        if (markersAndStations != null && !markersAndStations.isEmpty()) {
            ArrayAdapter<Station> adapter = new ArrayAdapter<Station>(MapActivity.this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>(markersAndStations.values()));
            searchView.setAdapter(adapter);
        }
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
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void getMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapMapActivity);
        mapFragment.getMapAsync(this);
    }

    private void getLocationServices() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void setUpFilterWidgets() {
        filterButton = (ImageButton) findViewById(R.id.filterButton);

        filterTextView = (TextView) findViewById(R.id.filterTextView);

        getRecycableMaterials();

        filterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterButton.performClick();
            }
        });

    }

    private void getRecycableMaterials() {
        BaseApiService api = RetrofitClient.getApiService();
        Call<List<Material>> call = api.getMaterials();
        List<Material> materials;

        call.enqueue(new Callback<List<Material>>() {
            @Override
            public void onResponse(Call<List<Material>> call, Response<List<Material>> response) {
                if (response.isSuccessful()) {

                    List<Material> materials = response.body();

                    //all materials plus the "Select all" item
                    noOfFilterItems = materials.size() + 1;

                    filterItems = new String[noOfFilterItems];

                    filterItems[0] = getString(R.string.SELECT_ALL_MATERIALS);

                    for (int i = 1; i < noOfFilterItems; i++) {
                        for (int j = 0; j < i; j++) {
                            Material material = materials.get(j);
                            String name = material.getName();
                            filterItems[i] = name;
                        }
                    }

                    checkedFilterOptions = new boolean[noOfFilterItems];
                    for (int i = 0; i < noOfFilterItems; i++) {
                        checkedFilterOptions[i] = true;
                    }

                } else {
                    Log.d(TAG, "Server response code: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<List<Material>> call, Throwable t) {
                filterButton.setEnabled(false);
                filterTextView.setEnabled(false);
                Log.d(TAG, "failed to get materials from REST service");
                Toast.makeText(getApplicationContext(), R.string.API_fail_get_materials, Toast.LENGTH_LONG).show();
            }

        });
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(getApplicationContext(), OverviewActivity.class);
        intent.putExtra("user", loggedInUser);
        startActivity(intent);
    }

    private void goToGuide() {
        Intent intent = new Intent(getApplicationContext(), GuideMainActivity.class);
        intent.putExtra("user", loggedInUser);
        startActivity(intent);
    }

    private void goToStation(Station station) {
        Intent stationIntent = new Intent(this, StationActivity.class);
        Bundle bundle = new Bundle();
        stationIntent.putExtra("station", station);
        stationIntent.putExtra("user", loggedInUser);
        bundle.putDouble("camera center latitude", map.getCameraPosition().target.latitude);
        bundle.putDouble("camera center longitude", map.getCameraPosition().target.longitude);
        bundle.putFloat("camera zoom level", map.getCameraPosition().zoom);
        stationIntent.putExtras(bundle);
        startActivity(stationIntent);
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

        getRecyclingStations();

        addMarkerListener();

        ensureAccessToLocation();

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

    private void addMarkerListener() {
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //show station info based on the marker clicked

                if (lastClicked == null) {
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.selectedstation_marker));
                    lastClicked = marker;

                } else {
                    //restore to default icon
                    lastClicked.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.defaultstation_marker));
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.defaultstation_marker));
                    lastClicked = null;
                }

                Station station = markersAndStations.get(marker);
                Position position = station.getPosition();
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(position.getX(), position.getY()), DEFAULT_ZOOM));

                goToStation(station);

                return true;
            }
        });
    }

    private void getRecyclingStations() {
        BaseApiService api = RetrofitClient.getApiService();
        Call<List<Station>> call = api.getStations();

        call.enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if (response.isSuccessful()) {
                    List<Station> stations = response.body();
                    Log.d(TAG, "OnResponse() got: " + stations.toString());
                    for (Station s : stations) {
                        Position p = s.getPosition();
                        LatLng latlong = new LatLng(p.getX(), p.getY());
                        Marker marker = map.addMarker(new MarkerOptions().position(latlong).title(p.toString()).snippet("").icon(BitmapDescriptorFactory.fromResource(R.drawable.defaultstation_marker)));
                        marker.hideInfoWindow();
                        markersAndStations.put(marker, s);
                    }
                    setUpAutoCompleteSearch();
                } else {
                    Log.d(TAG, "Server response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                Log.d(TAG, "API call to get stations failed");
                Toast.makeText(getApplicationContext(), R.string.failed_to_get_recycling_stations, Toast.LENGTH_LONG).show();
            }
        });


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

    public void onFilterDialog(View v) {
        dialog = onCreateFilterDialog(null);
        dialog.show();

        final ListView listView = dialog.getListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean isChecked = listView.isItemChecked(position);

                if (position == 0) {
                    if (selectAll) {
                        for (int i = 1; i < noOfFilterItems; i++) { // we start with first element after "Select all" choice
                            if (isChecked && !listView.isItemChecked(i)
                                    || !isChecked && listView.isItemChecked(i)) {
                                listView.performItemClick(listView, i, 0);
                            }
                        }
                    }
                } else {
                    if (!isChecked && listView.isItemChecked(0)) {
                        // if other item is unselected while "Select all" is selected, unselect "Select all"
                        // operations false, performItemClick, true is a must in order for this code to work
                        selectAll = false;
                        listView.performItemClick(listView, 0, 0);
                        selectAll = true;
                    }
                }
                //this makes sure we remember which items are checked
                checkedFilterOptions[position] = isChecked;
            }
        });
    }

    private void filterStationsDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

    }

    public AlertDialog onCreateFilterDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);

        builder.setTitle(R.string.filterDialogHeader)
                .setMultiChoiceItems(filterItems, checkedFilterOptions, null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        filterMarkersOnMap();
                    }
                });
        return builder.create();
    }

    private void filterMarkersOnMap() {
        if (allMaterialsAreSelected()) {

        } else {

        }
    }

    private boolean allMaterialsAreSelected() {
        return checkedFilterOptions[0] = true;
    }

}


