package com.example.edvin.app.map;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edvin.app.R;
import com.example.edvin.app.guide.GuideMainActivity;
import com.example.edvin.app.models.CleaningSchedule;
import com.example.edvin.app.models.LoggedInUser;
import com.example.edvin.app.models.Material;
import com.example.edvin.app.models.MaterialSchedule;
import com.example.edvin.app.models.Report;
import com.example.edvin.app.models.Station;
import com.example.edvin.app.models.User;
import com.example.edvin.app.models.UserAccount;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
    private BottomNavigationView bottomNavigationView;
    private final String TAG = "REGO: StationActivity";
    private int[] materialImages;
    private List<Integer> reportImages;
    private List<String> reportTitles;
    private List<String> reportDescriptions;
    private List<Material> materials;
    private GridView materialsGrid;
    private ListView reportList;
    private GeoApiContext apiContext;
    private Button report;
    private List<String> selectedReportTypes;
    private List<String> selectedFullMaterials;
    private String[] reportTypeItems;
    private String[] materialItems;
    private final int FULL = 0;
    private Button reportDialog_positive;
    private Button materialDialog_positive;
    private ImageButton close;
    private TextView stationName;
    private final int REPORTS_API_PATH_START = 45; //index locating station name in API call to server for reports
    private TextView postalAddress;
    private UserAccount account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        getMap();

        setUpCloseButton();

        getIntentAndBundle();

        setUpStationTextView();

        setUpMaterialGrid();

        setUpBottomNavigationView();

        getReports();

        setUpReportButton();

    }


    private void setUpStationTextView() {
        stationName = findViewById(R.id.stationName);
        stationName.setText(station.getStationName());
        postalAddress = findViewById(R.id.postalAddress);
        postalAddress.setText(station.getZipCode() + " " + station.getArea());

        stationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close.performClick();
            }
        });
    }

    private void setUpCloseButton() {
        close = findViewById(R.id.closeStationInfo);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMap();
            }
        });


    }

    private void setUpReportButton() {
        report = findViewById(R.id.reportButton);

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReportDialog();
            }
        });

    }


    private void openReportDialog() {

        if (reportTypeItems == null) {
            reportTypeItems = new String[]{getString(R.string.report_item_full), getString(R.string.report_item_needs_cleaning)};
        }

        if (selectedReportTypes == null) {
            selectedReportTypes = new ArrayList<>();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(StationActivity.this);

        builder.setTitle(R.string.report_dialog_header);

        builder.setMultiChoiceItems(reportTypeItems, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {

                    selectedReportTypes.add(reportTypeItems[which]);

                    reportDialog_positive.setEnabled(true);

                } else if (selectedReportTypes.contains(reportTypeItems[which])) {

                    selectedReportTypes.remove(reportTypeItems[which]);

                    if (selectedReportTypes.isEmpty()) {
                        reportDialog_positive.setEnabled(false);
                    }
                }
            }
        }).setPositiveButton(R.string.positive_option_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                if (selectedReportTypes.contains(reportTypeItems[FULL])) {
                    openMaterialDialog();
                } else {
                    sendReport();
                }
            }
        })
                .setNegativeButton(R.string.cancel_option_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        selectedReportTypes.clear();
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

        reportDialog_positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        reportDialog_positive.setEnabled(false);


    }

    private void openMaterialDialog() {
        if (selectedFullMaterials == null) {
            selectedFullMaterials = new ArrayList<>();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(StationActivity.this);

        builder.setTitle(R.string.materials_dialog_header);

        builder.setMultiChoiceItems(materialItems, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {

                    selectedFullMaterials.add(materialItems[which]);

                    materialDialog_positive.setEnabled(true);

                } else if (selectedFullMaterials.contains(materialItems[which])) {

                    selectedFullMaterials.remove(materialItems[which]);

                    if (selectedFullMaterials.isEmpty()) {
                        materialDialog_positive.setEnabled(false);
                    }
                }
            }
        }).setPositiveButton(R.string.positive_option_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                sendReport();

            }
        })
                .setNegativeButton(R.string.cancel_option_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        selectedReportTypes.clear();
                        selectedFullMaterials.clear();
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

        materialDialog_positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        materialDialog_positive.setEnabled(false);


    }

    private void sendReport() {

        UserAccount user = getUserAccount(loggedInUser);

        CleaningSchedule cleaningSchedule = null;
        List<MaterialSchedule> materialSchedules = null;

        if (selectedReportTypes.contains(getString(R.string.report_item_full))) {

            for (MaterialSchedule ms : station.getMaterialSchedules()) {

                Material m = ms.getMaterial();

                if (selectedFullMaterials.contains(m.getName())) {

                    if (materialSchedules == null) {

                        materialSchedules = new ArrayList<>();
                    }

                    materialSchedules.add(new MaterialSchedule(ms.getDate(), ms.getMaterial()));
                }

            }

        }

        if (selectedReportTypes.contains(getString(R.string.report_item_needs_cleaning))) {
            cleaningSchedule = station.getCleaningSchedule();
        }

        Report newReport = new Report(station, user, materialSchedules, cleaningSchedule);

        postReport(newReport);

        if (selectedReportTypes != null) {
            selectedReportTypes.clear();
        }

        if (selectedFullMaterials != null) {
            selectedFullMaterials.clear();
        }

        confirmReportToUser();
    }

    private UserAccount getUserAccount(LoggedInUser user) {
        BaseApiService api = RetrofitClient.getApiService();

//        Call<UserAccount> call = api.getUserAccount(user.getId());
// @TODO replace below with above before launch of app

        Call<UserAccount> call = api.getUserAccount(1);

        call.enqueue(new Callback<UserAccount>() {
            @Override
            public void onResponse(Call<UserAccount> call, Response<UserAccount> response) {
                if (response.isSuccessful()) {
                    account = response.body();
                } else {
                    Log.d(TAG, String.valueOf(response.code()));
                }

            }

            @Override
            public void onFailure(Call<UserAccount> call, Throwable t) {
                Log.d(TAG, "Failed to get user account from server: " + t.toString());
            }
        });

    return account;

    }

    private void postReport(Report report) {
        BaseApiService api = RetrofitClient.getApiService();

        Call<Report> call = api.postReport(report);

        call.enqueue(new Callback<Report>() {
            @Override
            public void onResponse(Call<Report> call, Response<Report> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Successfully posted report");
                }
            }

            @Override
            public void onFailure(Call<Report> call, Throwable t) {
                Log.d(TAG, "Successfully posted report");
                Toast toast = Toast.makeText(StationActivity.this,"Rapporten kunde inte skapas, ursäkta oss", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    private void confirmReportToUser() {
        Toast toast = Toast.makeText(StationActivity.this, "Tack för din feedback!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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

            materialItems = new String[materials.size()];

            for (int i = 0; i < materials.size(); i++) {

                String name = materials.get(i).getName();

                materialItems[i] = name;

                name = name.toLowerCase().trim();

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

        String stationName = station.getStationName();

        Call<List<Report>> call = api.getReportsForStation(stationName);

        call.enqueue(new Callback<List<Report>>() {
            @Override
            public void onResponse(Call<List<Report>> call, Response<List<Report>> response) {

                reportImages = new ArrayList<>();
                reportTitles = new ArrayList<>();
                reportDescriptions = new ArrayList<>();

                if (response.isSuccessful()) {

                    Map<String, Integer> frequencyMap = new HashMap<>();

                    List<Report> reports = response.body();

                    if (reports != null && !reports.isEmpty()) {

                        for (Report r : reports) {

                            CleaningSchedule cleaningSchedule = r.getCleaningSchedule();

                            if (cleaningSchedule != null) {

                                if (frequencyMap.containsKey(getString(R.string.NEEDS_CLEANING))) {

                                    frequencyMap.put(getString(R.string.NEEDS_CLEANING), frequencyMap.get(getString(R.string.NEEDS_CLEANING)) + 1);

                                } else {

                                    frequencyMap.put(getString(R.string.NEEDS_CLEANING), 1);
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

                        Log.d(TAG, frequencyMap.entrySet().toString());

                        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
                            String reportType = entry.getKey();
                            int noOfReports = entry.getValue();

                            if (!reportTitles.contains(reportType)) {

                                if (reportType.equals(getString(R.string.NEEDS_CLEANING))) {

                                    reportTitles.add(getString(R.string.item_text_report_type_needs_cleaning) + reportType);

                                } else {

                                    reportType = reportType.substring(0, 1).toUpperCase() + reportType.substring(1);
                                    reportTitles.add(reportType + getString(R.string.item_text_report_type_full));

                                }

                                reportImages.add(R.drawable.report_error);
                            }

                        }

                    } else {
                        reportTitles.add(getString(R.string.report_item_no_reports));
                        reportDescriptions.add("");
                        reportImages.add(R.drawable.report_ok);
                    }

                    ReportAdapter adapter = new ReportAdapter(getApplicationContext(), reportImages, reportTitles);
                    reportList = (ListView) findViewById(R.id.reports);
                    reportList.setAdapter(adapter);

                } else {
                    Log.d(TAG, "Not successful when retrieving reports, server response code: " + response.code());
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
