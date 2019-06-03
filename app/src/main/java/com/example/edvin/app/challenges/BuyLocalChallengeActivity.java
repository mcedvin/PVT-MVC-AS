package com.example.edvin.app.challenges;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.CircularPropagation;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.edvin.app.R;
import com.example.edvin.app.logininterface.SecondActivity;
import com.example.edvin.app.logininterface.SignupActivity;
import com.example.edvin.app.models.Challenge;
import com.example.edvin.app.models.LoggedInUser;
import com.example.edvin.app.models.User;
import com.example.edvin.app.util.BaseApiService;
import com.example.edvin.app.util.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.nihaskalam.progressbuttonlibrary.CircularProgressButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyLocalChallengeActivity extends AppCompatActivity {

    ImageButton backButton;
    final String challengeName = "BuyLocal";
    final int thisChallengeCategory = 3;

    BaseApiService api = RetrofitClient.getApiService();
    LoggedInUser loggedInUser;
    Challenge thisChallenge;
    Gson gson = new Gson();
    SharedPreferences sharedPreferences;

    private CircularProgressButton cpb;

    //TODO: FIX THE UPDATING of the 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_local_challenge);
        //loggedInUser = (LoggedInUser) getIntent().getExtras().getSerializable(getString(R.string.INTENT_KEY_USER));
        getTheUser();
        System.out.println(loggedInUser.hasCurrent(challengeName));


        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(l -> goBack());
        cpb = findViewById(R.id.circularButton);
        checkTheGet();

        setTheButton();







            //cpb.setIndeterminateProgressMode(true);
            cpb.setIndeterminateProgressMode(false);
            cpb.setSweepDuration(2000);
            cpb.showIdle();
            cpb.setStrokeColor(ContextCompat.getColor(this, R.color.colorStroke));

    }

    public void onClickProgress(View view) {

        if (cpb.isIdle()) {
            cpb.showProgress();


            if(cpb.getText().equals("Gå Med")){
                //TODO: connect to userAccount!

                acceptThisChallenge();

                updateThePreferences();

            }else if(cpb.getText().equals("Slutför")){
                //TODO: DELETE FRÅN CURRENT AND ADD TO COMPLETED

                completeThisChallenge();
                updateThePreferences();
            }




        } else if (cpb.isErrorOrCompleteOrCancelled()) {
            cpb.setOnClickListener(null);
        }
        else
            cpb.showComplete();

    }



    public void checkTheGet(){


        Call<Challenge> call = api.getChallenge(challengeName);

        call.enqueue(new Callback<Challenge>() {
            @Override
            public void onResponse(Call<Challenge> call, Response<Challenge> response) {
                if (!response.isSuccessful()) {
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(BuyLocalChallengeActivity.this, response.code() + ":not found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(BuyLocalChallengeActivity.this, response.code() + ":server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(BuyLocalChallengeActivity.this, response.code() + ":unknown error", Toast.LENGTH_SHORT).show();
                            break;

                    }
                }else{
                    thisChallenge = response.body();


                    if(thisChallenge!=null) {
                        Toast.makeText(BuyLocalChallengeActivity.this, "Get completed!", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(BuyLocalChallengeActivity.this, "Not found", Toast.LENGTH_SHORT).show();
                        putThisChallenge(); //TODO: check to put! here ? or by 404? or skit i det och just put it ?
                    }


                }


            }

            @Override
            public void onFailure(Call<Challenge> call, Throwable t) {
                Toast.makeText(BuyLocalChallengeActivity.this,"FAILURE!"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });



    }


    public void putThisChallenge(){

       thisChallenge = new Challenge(challengeName,"description",10,null,thisChallengeCategory);
    Call<Void> call = api.putChallenge(thisChallenge);


                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!response.isSuccessful()) {
                            switch (response.code()) {
                                case 404:
                                    Toast.makeText(BuyLocalChallengeActivity.this, response.code() + ":not found:PUT", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(BuyLocalChallengeActivity.this, response.code() + ":server broken:PUT", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(BuyLocalChallengeActivity.this, response.code() + ":unknown error:PUT", Toast.LENGTH_SHORT).show();
                                    break;

                            }
                            cpb.showError();
                        } else {
                            Toast.makeText(BuyLocalChallengeActivity.this, "Put Success! " + response.code() + "yay!", Toast.LENGTH_SHORT).show();

                            cpb.showComplete();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        if (t instanceof EOFException) {
                            Toast.makeText(BuyLocalChallengeActivity.this, "put Success!", Toast.LENGTH_SHORT).show();
                            cpb.showComplete();
                        } else {
                            Toast.makeText(BuyLocalChallengeActivity.this, "put Failure!", Toast.LENGTH_SHORT).show();
                            cpb.showError();
                        }
                    }

                });
    }


    public void acceptThisChallenge(){

        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive(Integer.toString(loggedInUser.getId())));
        array.add(new JsonPrimitive(challengeName));


        Call<Void> call = api.acceptChallenge(array); // ? checking




        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(BuyLocalChallengeActivity.this, response.code() + ":not found:PUT", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(BuyLocalChallengeActivity.this, response.code() + ":server broken:PUT", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(BuyLocalChallengeActivity.this, response.code() + ":unknown error:PUT", Toast.LENGTH_SHORT).show();
                            break;

                    }
                    cpb.showError();
                    cpb.setOnClickListener(null);
                } else {
                    Toast.makeText(BuyLocalChallengeActivity.this, "Challenge accepted! " + response.code() + "yay!", Toast.LENGTH_SHORT).show();

                    cpb.showComplete();
                    cpb.setOnClickListener(null);


                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (t instanceof EOFException) {
                    Toast.makeText(BuyLocalChallengeActivity.this, "challgnge accepted", Toast.LENGTH_SHORT).show();
                    cpb.showComplete();
                    cpb.setOnClickListener(null);
                } else {
                    Toast.makeText(BuyLocalChallengeActivity.this, "accept Failure!", Toast.LENGTH_SHORT).show();
                    cpb.showError();
                    cpb.setOnClickListener(null);
                }
            }

        });
    }


    public void setTheButton(){
        if (loggedInUser.hasCurrent(challengeName)) {
            cpb.setText("Slutför");
            System.out.println(cpb.getText());
            cpb.setBackgroundColor(Color.BLACK);
            cpb.setStrokeColor(Color.BLACK);
        }else

        if (loggedInUser.hasCompleted(challengeName)){
            cpb.setText("Avslutad");
            System.out.println(cpb.getText());
            cpb.setBackgroundColor(Color.BLUE);
            cpb.setStrokeColor(Color.BLACK);
            cpb.setOnClickListener(null);
        }
    }


    public void goBack(){



            Intent mintent = new Intent(this, AnimalsActivity.class);
            //mintent.putExtra(getString(R.string.INTENT_KEY_USER),loggedInUser);
            startActivity(mintent);

    }

    public void updateThePreferences(){
        if (cpb.getText().equals("Gå Med"))
            loggedInUser.getCurrentChallenges().add(challengeName);
        else {
            loggedInUser.getCompletedChallenges().add(challengeName);
            loggedInUser.getCurrentChallenges().remove(challengeName);
        }



        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson2 = new Gson();
        String json = gson2.toJson(loggedInUser);
        prefsEditor.putString("SerializableObject", json);
        prefsEditor.apply();
    }

    public void getTheUser(){
        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        int j = sharedPreferences.getInt("key", 0);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("SerializableObject", "");
        loggedInUser = gson.fromJson(json, LoggedInUser.class);
        System.out.println(loggedInUser.getId());
    }


    public void completeThisChallenge(){

        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive(Integer.toString(loggedInUser.getId())));
        array.add(new JsonPrimitive(challengeName));


        Call<Void> call = api.acceptChallenge(array); // ? checking




        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(BuyLocalChallengeActivity.this, response.code() + ":not found:PUT", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(BuyLocalChallengeActivity.this, response.code() + ":server broken:PUT", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(BuyLocalChallengeActivity.this, response.code() + ":unknown error:PUT", Toast.LENGTH_SHORT).show();
                            break;

                    }
                    cpb.showError();
                    cpb.setOnClickListener(null);
                } else {
                    Toast.makeText(BuyLocalChallengeActivity.this, "Challenge completed! " + response.code() + "yay!", Toast.LENGTH_SHORT).show();

                    cpb.showComplete();
                    cpb.setOnClickListener(null);


                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (t instanceof EOFException) {
                    Toast.makeText(BuyLocalChallengeActivity.this, "challgnge completed", Toast.LENGTH_SHORT).show();
                    cpb.showComplete();
                    cpb.setOnClickListener(null);
                } else {
                    Toast.makeText(BuyLocalChallengeActivity.this, "accept Failure!", Toast.LENGTH_SHORT).show();
                    cpb.showError();
                    cpb.setOnClickListener(null);
                }
            }

        });


    }


}
