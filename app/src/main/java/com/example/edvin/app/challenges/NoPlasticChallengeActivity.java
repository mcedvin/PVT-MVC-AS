package com.example.edvin.app.challenges;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.edvin.app.R;
import com.example.edvin.app.models.Challenge;
import com.example.edvin.app.models.LoggedInUser;
import com.example.edvin.app.util.BaseApiService;
import com.example.edvin.app.util.RetrofitClient;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.nihaskalam.progressbuttonlibrary.CircularProgressButton;

import java.io.EOFException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoPlasticChallengeActivity extends AppCompatActivity {

    ImageButton backButton;
    private Bitmap image;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private LoginManager loginManager;
    LoggedInUser loggedInUser;
    SharedPreferences sharedpreferences;
    final String challengeName = "NoPlast";
    final int thisChallengeCategory = 3;
    BaseApiService api = RetrofitClient.getApiService();
    Challenge thisChallenge;
    Gson gson = new Gson();
    private CircularProgressButton cpb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_plastic_challenge);



        getTheUser();
        cpb = findViewById(R.id.circularButton);
        checkTheGet();
        setTheButton();



        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(l -> goBack());

        cpb.setIndeterminateProgressMode(false);
        cpb.setSweepDuration(2000);
        cpb.showIdle();
        cpb.setStrokeColor(ContextCompat.getColor(this, R.color.colorStroke));


        image = image = BitmapFactory.decodeResource(getResources(), R.drawable.bakgrund);

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareButton shareButton = (ShareButton) findViewById(R.id.fb_share_button);
        shareButton.setShareContent(content);
    }

    public void share(View view) {


        shareDialog = new ShareDialog(this);


        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://ibb.co/P9RbS11"))
                    .build();
            shareDialog.show(linkContent);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void getTheUser(){
        sharedpreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        int j = sharedpreferences.getInt("key", 0);

        Gson gson = new Gson();
        String json = sharedpreferences.getString("SerializableObject", "");
        loggedInUser = gson.fromJson(json, LoggedInUser.class);
        System.out.println(loggedInUser.getId());
    }

    public void checkTheGet(){


        Call<Challenge> call = api.getChallenge(challengeName);

        call.enqueue(new Callback<Challenge>() {
            @Override
            public void onResponse(Call<Challenge> call, Response<Challenge> response) {
                if (!response.isSuccessful()) {
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(NoPlasticChallengeActivity.this, response.code() + ":not found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(NoPlasticChallengeActivity.this, response.code() + ":server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(NoPlasticChallengeActivity.this, response.code() + ":unknown error", Toast.LENGTH_SHORT).show();
                            break;

                    }
                }else{
                    thisChallenge = response.body();


                    if(thisChallenge!=null) {
                        Toast.makeText(NoPlasticChallengeActivity.this, "Utmaningen laddades!", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(NoPlasticChallengeActivity.this, "Not found", Toast.LENGTH_SHORT).show();
                        putThisChallenge(); //TODO: check to put! here ? or by 404? or skit i det och just put it ?
                    }


                }


            }

            @Override
            public void onFailure(Call<Challenge> call, Throwable t) {
                Toast.makeText(NoPlasticChallengeActivity.this,"FAILURE!"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });



    }


    public void putThisChallenge(){

        thisChallenge = new Challenge(challengeName,"description",7,null,thisChallengeCategory);
        Call<Void> call = api.putChallenge(thisChallenge);


        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(NoPlasticChallengeActivity.this, response.code() + ":not found:PUT", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(NoPlasticChallengeActivity.this, response.code() + ":server broken:PUT", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(NoPlasticChallengeActivity.this, response.code() + ":unknown error:PUT", Toast.LENGTH_SHORT).show();
                            break;

                    }
                    cpb.showError();
                } else {
                    Toast.makeText(NoPlasticChallengeActivity.this, "Put Success! " + response.code() + "yay!", Toast.LENGTH_SHORT).show();

                    cpb.showComplete();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (t instanceof EOFException) {
                    Toast.makeText(NoPlasticChallengeActivity.this, "put Success!", Toast.LENGTH_SHORT).show();
                    cpb.showComplete();
                } else {
                    Toast.makeText(NoPlasticChallengeActivity.this, "put Failure!", Toast.LENGTH_SHORT).show();
                    cpb.showError();
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



        Intent mintent = new Intent(this, OceanActivity.class);
        //mintent.putExtra(getString(R.string.INTENT_KEY_USER),loggedInUser);
        startActivity(mintent);

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
                            Toast.makeText(NoPlasticChallengeActivity.this, response.code() + ":not found:PUT", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(NoPlasticChallengeActivity.this, response.code() + ":server broken:PUT", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(NoPlasticChallengeActivity.this, response.code() + ":unknown error:PUT", Toast.LENGTH_SHORT).show();
                            break;

                    }
                    cpb.showError();
                    cpb.setOnClickListener(null);
                } else {
                    Toast.makeText(NoPlasticChallengeActivity.this, "Utmaningen är antagen! ", Toast.LENGTH_SHORT).show();

                    cpb.showComplete();
                    cpb.setOnClickListener(null);


                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (t instanceof EOFException) {
                    Toast.makeText(NoPlasticChallengeActivity.this, "challgnge accepted", Toast.LENGTH_SHORT).show();
                    cpb.showComplete();
                    cpb.setOnClickListener(null);
                } else {
                    Toast.makeText(NoPlasticChallengeActivity.this, "accept Failure!", Toast.LENGTH_SHORT).show();
                    cpb.showError();
                    cpb.setOnClickListener(null);
                }
            }

        });
    }

    public void updateThePreferences(){
        if (cpb.getText().equals("Gå Med"))
            loggedInUser.getCurrentChallenges().add(challengeName);
        else {
            loggedInUser.getCompletedChallenges().add(challengeName);
            loggedInUser.getCurrentChallenges().remove(challengeName);
        }



        SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
        Gson gson2 = new Gson();
        String json = gson2.toJson(loggedInUser);
        prefsEditor.putString("SerializableObject", json);
        prefsEditor.apply();
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
                            Toast.makeText(NoPlasticChallengeActivity.this, response.code() + ":not found:PUT", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(NoPlasticChallengeActivity.this, response.code() + ":server broken:PUT", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(NoPlasticChallengeActivity.this, response.code() + ":unknown error:PUT", Toast.LENGTH_SHORT).show();
                            break;

                    }
                    cpb.showError();
                    cpb.setOnClickListener(null);
                } else {
                    Toast.makeText(NoPlasticChallengeActivity.this, "Utmaningen är avslutat. Bra jobbat! ", Toast.LENGTH_SHORT).show();

                    cpb.showComplete();
                    cpb.setOnClickListener(null);


                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (t instanceof EOFException) {
                    Toast.makeText(NoPlasticChallengeActivity.this, "challgnge completed", Toast.LENGTH_SHORT).show();
                    cpb.showComplete();
                    cpb.setOnClickListener(null);
                } else {
                    Toast.makeText(NoPlasticChallengeActivity.this, "accept Failure!", Toast.LENGTH_SHORT).show();
                    cpb.showError();
                    cpb.setOnClickListener(null);
                }
            }

        });


    }


}


