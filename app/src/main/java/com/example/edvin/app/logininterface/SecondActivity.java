package com.example.edvin.app.logininterface;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edvin.app.models.Challenge;
import com.example.edvin.app.models.ChallengeAccepted;
import com.example.edvin.app.models.LoggedInUser;
import com.example.edvin.app.overview.OverviewActivity;
import com.example.edvin.app.settings.SettingsActivity;
import com.example.edvin.app.util.InternetConnection;
import com.example.edvin.app.models.User;
import com.example.edvin.app.util.BaseApiService;
import com.example.edvin.app.util.RetrofitClient;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.example.edvin.app.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondActivity extends AppCompatActivity {

    EditText passTxt;

    EditText userTxt;
    Button startBtn;
    boolean check = false;
    LoggedInUser loggedInUser;
    SharedPreferences sharedPreferences;
    private LoginButton loginButton;


    AccessTokenTracker tokenTracker;
    CallbackManager callbackManager;
    TextView txtEmail, txtBirthday, txtFriends;
    ProgressDialog mDialog;
    ImageView imgAvatar;
    private com.facebook.FacebookSdk FacebookSdk;


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);


        passTxt = findViewById(R.id.passTxt);
        userTxt = findViewById(R.id.userTxt);
        startBtn = findViewById(R.id.startBtn);



        /*
        callbackManager = CallbackManager.Factory.create();
        txtBirthday = (TextView) findViewById(R.id.txtBirthday);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtFriends = (TextView) findViewById(R.id.txtFriends);
        imgAvatar = (ImageView) findViewById(R.id.avatar);


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mDialog = new ProgressDialog(SecondActivity.this);
                mDialog.setMessage("Retrieving data...");
                mDialog.show();


                String accesstoken = loginResult.getAccessToken().getToken();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        mDialog.dismiss();
                        Log.d("response", object.toString());
                        // Get facebook data from login
                        Bundle bFacebookData = getFacebookData(object);

                    }
                });

                //Request Graph API
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email");
                request.setParameters(parameters);
                request.executeAsync();
                Toast.makeText(SecondActivity.this,parameters.getString("first_name"),Toast.LENGTH_SHORT).show();




            }

            @Override
            public void onCancel() {


            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        //If already logged in
        if (AccessToken.getCurrentAccessToken() != null) {
            //Set the userID
            txtEmail.setText(AccessToken.getCurrentAccessToken().getUserId());
        }

    }


    @Override
    public void onDestroy() {
        tokenTracker.stopTracking();
        super.onDestroy();
    }
*/
    }



    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {

                bundle.putString("idFacebook", id);
                if (object.has("first_name"))
                    bundle.putString("first_name", object.getString("first_name"));
                if (object.has("last_name"))
                    bundle.putString("last_name", object.getString("last_name"));
                if (object.has("email")) {
                    bundle.putString("email", object.getString("email"));
                    System.out.println(object.getString("email"));
                    System.out.println(bundle.getString("email"));
                }

                return bundle;
            } catch (JSONException e) {
                Log.d("", "Error parsing JSON");
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

/*

JOELSSSSS
    private void getData(JSONObject object) {
        try {

            URL profile_picture = new URL("https://graph.facebook.com/" + object.getString("id") + "/picture?width=250&height=250");
           Picasso.with(this).load(profile_picture.toString()).into(imgAvatar);

            txtEmail.setText(object.getString("Email"));
            txtBirthday.setText(object.getString("Birthday"));
            txtFriends.setText("Friends: " + object.getJSONObject("friends").getJSONObject("summary").getString("total_count"));


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
*/

    public void onClick(View view) {


        if(InternetConnection.checkConnection(getApplicationContext())){

            final ProgressDialog dialog;
            final String username = userTxt.getText().toString();
            final String password = passTxt.getText().toString();




            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(SecondActivity.this);
            dialog.setTitle("Hämtar JSON data");
            dialog.setMessage("Ett ögonblick...");
            dialog.show();



            //Creating an object of our api interface
            BaseApiService api = RetrofitClient.getApiService();


            /**
             * Calling JSON
             */
            //Call<List<User>> call = api.getUsers();
            //String[] params = {username, password};
            String path = username+"+"+password;
            Call<User> call = api.getAuthenticated(path);
            //Call<List<User>> call = api.getUsersDontCheckThePassword();


            /**
             * Enqueue Callback will be call when get response...
             */





            /**
             * call v2 : new implementation
             */
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    dialog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(SecondActivity.this,"Code: " + response.code(),Toast.LENGTH_SHORT).show();
                        return;
                    }
                    /**
                     * Got Successfully
                     */



                    User checkedUser = response.body();

                    if (checkedUser== null)
                        Toast.makeText(SecondActivity.this, "användaren finns inte!",Toast.LENGTH_SHORT).show();
                    else{
                        Toast.makeText(SecondActivity.this, "Välkommen "+checkedUser.getFirstName()+"! Loggar in på ditt konto..",Toast.LENGTH_SHORT).show();
                    check = true;


                    loggedInUser = new LoggedInUser(checkedUser.getFirstName()+" "+checkedUser.getLastName(),checkedUser.getUserAccount().getId(),
                            checkedUser.getUserAccount().getCurrentChallenges(),checkedUser.getUserAccount().getCompletedChallenges());

                    sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(loggedInUser);
                    prefsEditor.putString("SerializableObject", json);
                    prefsEditor.putInt("key",1);
                    prefsEditor.apply();
                    }


                    //connect to homepage

                    if(check){

                        goToHome();

                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(SecondActivity.this,"FAILURE!"+t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });


        }


    }

    protected void goToLogin(){

        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }


    protected void goToHome(){

        Intent intent = new Intent(this, OverviewActivity.class);
        intent.putExtra("serialize_data",loggedInUser);
        startActivity(intent);
    }





}