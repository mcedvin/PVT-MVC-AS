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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edvin.app.R;
import com.example.edvin.app.guide.GuideMainActivity;
import com.example.edvin.app.logininterface.MainActivity;
import com.example.edvin.app.models.CleaningSchedule;
import com.example.edvin.app.models.LoggedInUser;
import com.example.edvin.app.models.Material;
import com.example.edvin.app.models.MaterialSchedule;
import com.example.edvin.app.models.Report;
import com.example.edvin.app.models.Station;
import com.example.edvin.app.overview.OverviewActivity;
import com.example.edvin.app.util.BaseApiService;
import com.example.edvin.app.util.RetrofitClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.maps.GeoApiContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Station station;
    private LoggedInUser loggedInUser;
    private GoogleMap map;
    private double cameraCenterLatitude, cameraCenterLongitude;
    private float cameraZoomLevel;
    private AutoCompleteTextView searchView;
    private BottomNavigationView bottomNavigationView;
    private TextView filterTextView;
    private ImageButton filterButton;
    private final String TAG = "StationActivity";
    private int[] materialImages;
    private List<Integer> reportImages;
    private List<String> reportTitles;
    private List<String> reportDescriptions;
    private List<Material> materials;
    private GridView materialsGrid;
    private ListView reportList;
    private GeoApiContext apiContext;
    Button report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        getMap();

        ImageButton close = findViewById(R.id.closeStationInfo);

        TextView stationName = findViewById(R.id.stationName);

        getIntentAndBundle();

        stationName.setText(station.getStationName());

        setUpMaterialGrid();

        setUpBottomNavigationView();

        getReports();

        setUpReportButton();

    }

    private void setUpReportButton() {
        report = findViewById(R.id.reportButton);

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void getIntentAndBundle() {
        Intent intent = getIntent();

        station = (Station) intent.getSerializableExtra(getString(R.string.INTENT_KEY_STATION));
        loggedInUser = (LoggedInUser) intent.getSerializableExtra(getString(R.string.INTENT_KEY_USER));
        Bundle fromReferringActivity = intent.getExtras();
        cameraCenterLatitude = fromReferringActivity.getDouble(getString(R.string.INTENT_KEY_CAMERA_LAT));
        cameraCenterLongitude = fromReferringActivity.getDouble(getString(R.string.INTENT_KEY_CAMERA_LONG));
        cameraZoomLevel = fromReferringActivity.getFloat(getString(R.string.INTENT_KEY_CAMERA_ZOOM));
    }

    private void setUpMaterialGrid() {

        materials = (List) station.getAvailableMaterials();


        if (materials != null && !materials.isEmpty()) {

            materialImages = new int[materials.size()];

            for (int i = 0; i < materials.size(); i++) {

                String name = materials.get(i).getName().toLowerCase().trim();

                switch (name) {
                    case "blandplast":
                        materialImages[i] = R.drawable.plast;
                        break;
                    case "glas":
                        materialImages[i] = R.drawable.glas;
                        break;
                    case "kartong":
                        materialImages[i] = R.drawable.papper;
                        break;
                    case "metall":
                        materialImages[i] = R.drawable.metall;
                        break;
                    case "tidningar":
                        materialImages[i] = R.drawable.tidningar;
                        break;
                    default:
                        break;

                }
            }

            MaterialAdapter adapter = new MaterialAdapter(getApplicationContext(), materialImages);
            materialsGrid = (GridView) findViewById(R.id.materials);
            materialsGrid.setAdapter(adapter);

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

    private void getReports() {
        BaseApiService api = RetrofitClient.getApiService();
        Log.d(TAG, "Stationsnamn: " + station.getStationName());
        Call<List<Report>> call = api.getReports();
        List<Report> reports;

        call.enqueue(new Callback<List<Report>>() {
            @Override
            public void onResponse(Call<List<Report>> call, Response<List<Report>> response) {
                if (response.isSuccessful()) {

                    Map<String, Integer> frequencyMap = new HashMap<>();

                    List<Report> reports = response.body();

                    if (reports != null && !reports.isEmpty()) {

                        for (Report r : reports) {

                            CleaningSchedule cleaningSchedule = r.getCleaningSchedule();

                            if (cleaningSchedule != null) {

                                if (frequencyMap.containsKey("stökig")) {

                                    frequencyMap.put("stökig", frequencyMap.get("Stökig") + 1);

                                } else {

                                    frequencyMap.put("stökig", 1);
                                }
                            }

                            List<MaterialSchedule> materialSchedules = (List) r.getMaterialSchedules();

                            for (MaterialSchedule m : materialSchedules) {

                                String materialName = m.getMaterial().getName().toLowerCase().trim();

                                if (frequencyMap.containsKey(materialName)) {

                                    frequencyMap.put(materialName, frequencyMap.get(materialName) + 1);

                                } else {

                                    frequencyMap.put(materialName, 1);
                                }
                            }
                        }

                        reportImages = new ArrayList<>();
                        reportTitles = new ArrayList<>();
                        reportDescriptions = new ArrayList<>();

                        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
                            String report = entry.getKey();
                            int noOfReports = entry.getValue();

                            if (report.equals("stökig")) {

                                reportTitles.add("Felanmälan: Rapporterad som " + report);

                            } else {

                                reportTitles.add("Felanmälan: " + report + " rapporterad som full");

                            }
                            reportDescriptions.add("Rapporterat av " + noOfReports + " personer");

                            reportImages.add(R.drawable.report_error);
                        }

                    } else {
                        reportTitles.add("Inga aktuella felanmälningar");
                        reportDescriptions.add("");
                        reportImages.add(R.drawable.report_ok);
                    }

                    ReportAdapter adapter = new ReportAdapter(getApplicationContext(), reportImages, reportTitles, reportDescriptions);
                    reportList = (ListView) findViewById(R.id.reports);
                    reportList.setAdapter(adapter);

                } else {
                    Log.d(TAG, "Server response code: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<List<Report>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.API_fail_get_station_reports, Toast.LENGTH_LONG).show();
                if (t instanceof IOException) {
                    Log.d(TAG, "Failed to retrieve reports on station from server, problem with Internet connection: " + t.getMessage());
                } else {
                    Log.d(TAG, "Failed to retrieve reports on station from server, conversion issue: "
                            + t.getMessage() + ", " + t.toString() + ", " + t.getLocalizedMessage());
                }
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
