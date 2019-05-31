package com.example.edvin.app.challenges;

import android.content.Intent;
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
import com.nihaskalam.progressbuttonlibrary.CircularProgressButton;

import java.io.EOFException;

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


    private CircularProgressButton cpb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_local_challenge);
                //loggedInUser = (LoggedInUser) getIntent().getExtras().getSerializable(getString(R.string.INTENT_KEY_USER));



        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(l -> startActivity(new Intent(this, AnimalsActivity.class)));
        cpb = findViewById(R.id.circularButton);
        checkTheGet();

        //TODO: checkTheOngoing();






            //cpb.setIndeterminateProgressMode(true);
            cpb.setIndeterminateProgressMode(false);
            cpb.setSweepDuration(2000);
            cpb.showIdle();
            cpb.setStrokeColor(ContextCompat.getColor(this, R.color.colorStroke));

    }

    public void onClickProgress(View view) {

        if (cpb.isIdle()) {
            cpb.showProgress();


            if(cpb.getIdleText().equals("Gå Med")){
                //TODO: connect to userAccount!
            }else if(cpb.getIdleText().equals("Avsluta")){
                //TODO: DELETE FRÅN CURRENT AND ADD TO COMPLETED
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
/*
                        if(loggedInUser.hasCompleted(thisChallenge)) {
                            cpb.setIdleText("DONE!");
                            cpb.setBackgroundColor(Color.BLUE);
                            cpb.setOnClickListener(null);
                        }else if(loggedInUser.hasCurrent(thisChallenge)){
                            cpb.setIdleText("Avsluta");
                            cpb.setBackgroundColor(Color.BLACK);
                            cpb.setOnClickListener(null);
                           } */

                    }else {
                        Toast.makeText(BuyLocalChallengeActivity.this, "Not found", Toast.LENGTH_SHORT).show();
                        putThisChallenge(); //TODO: check to put! here ? or by 404
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

       thisChallenge = new Challenge(challengeName,"description",7,null,thisChallengeCategory);
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


}
