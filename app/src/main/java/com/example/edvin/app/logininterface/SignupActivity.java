package com.example.edvin.app.logininterface;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.edvin.app.R;
import com.example.edvin.app.models.LoggedInUser;
import com.example.edvin.app.models.UserAccount;
import com.example.edvin.app.overview.OverviewActivity;
import com.example.edvin.app.util.InternetConnection;
import com.example.edvin.app.models.User;
import com.example.edvin.app.util.BaseApiService;
import com.example.edvin.app.util.RetrofitClient;
import com.google.gson.Gson;

import java.io.EOFException;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    EditText fnameTxt;
    EditText lnameTxt;
    EditText passTxt1;
    EditText passTxt2;
    EditText mailTxt;
    Button signBtn;
    LoggedInUser loggedInUser;
    User newUser;
    SharedPreferences sharedPreferences;
    BaseApiService api = RetrofitClient.getApiService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fnameTxt = findViewById(R.id.fnameTxt);
        lnameTxt = findViewById(R.id.lnameTxt);
        passTxt1 = findViewById(R.id.passTxt1);
        passTxt2 = findViewById(R.id.passTxt2);
        mailTxt = findViewById(R.id.mailTxt);
        signBtn = findViewById(R.id.signBtn);


    }


    public void onClick(View view) {

        if (InternetConnection.checkConnection(getApplicationContext())) {

            final ProgressDialog dialog;
            final String firstName = fnameTxt.getText().toString();
            final String lastName = lnameTxt.getText().toString();
            final String email = mailTxt.getText().toString();
            final String password = passTxt1.getText().toString();

            final String pass2 = passTxt2.getText().toString();


            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(SignupActivity.this);
            dialog.setTitle("Getting JSON data");
            dialog.setMessage("Please wait...");
            dialog.show();



            //Creating an object of our api interface


            newUser = new User(firstName,lastName,email,password);


            /**
             * Calling JSON
             */
            //Call<User> call = api.createUser(newUser);
            Call<User> call = api.putUser(newUser);


            /**
             * Enqueue Callback will be call when get response...
             */

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {


                    dialog.dismiss();
                    if (!response.isSuccessful()) {
                        switch (response.code()) {
                            case 404:
                                Toast.makeText(SignupActivity.this, response.code()+":not found", Toast.LENGTH_SHORT).show();
                                break;
                            case 500:
                                Toast.makeText(SignupActivity.this, response.code()+":server broken", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(SignupActivity.this, response.code()+":unknown error", Toast.LENGTH_SHORT).show();
                                break;

                    }
                        return;
                    }


                    /**
                     * Posted Successfully
                     */

                    User postUser = new User(response.body().getFirstName(),response.body().getLastName(),response.body().getEmail(),response.body().getUserAccount().getPassword());
                    System.out.println(postUser.getFirstName());
                    Toast.makeText(SignupActivity.this, "Success! "+ response.code()+", Welcome "+postUser.getFirstName()+"!" ,Toast.LENGTH_SHORT).show();
                    if(makeLoggedInUser())
                        goToHome();


                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    dialog.dismiss();


                    if (t instanceof EOFException){
                        Toast.makeText(SignupActivity.this,"welcome "+ newUser.getFirstName(),Toast.LENGTH_SHORT).show();



                        if(makeLoggedInUser())
                            goToHome();

                    }else
                    Toast.makeText(SignupActivity.this,"Failure! No response: "+t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

    protected boolean makeLoggedInUser(){

        final String email = mailTxt.getText().toString();

        Call<User> call = api.getOneUser(email);




        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {


                if (!response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this,"Code: " + response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }
                /**
                 * Got Successfully
                 */



                User postedUser = response.body();

                if (postedUser== null)
                    Toast.makeText(SignupActivity.this, "no such user!",Toast.LENGTH_SHORT).show();
                else{


                    loggedInUser = new LoggedInUser(postedUser.getFirstName()+" "+postedUser.getLastName(),postedUser.getUserAccount().getId(),
                            postedUser.getUserAccount().getCurrentChallenges(),postedUser.getUserAccount().getCompletedChallenges());

                    sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(loggedInUser);
                    prefsEditor.putString("SerializableObject", json);
                    prefsEditor.putInt("key",1);
                    prefsEditor.apply();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Toast.makeText(SignupActivity.this,"FAILURE!"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        if (loggedInUser==null)
            return true;
        else
            return false;
    }


    protected void goToHome(){

        Intent intent = new Intent(this, OverviewActivity.class);
        intent.putExtra(getString(R.string.INTENT_KEY_USER),loggedInUser);
        startActivity(intent);
    }


    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        //TODO check the other ids

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
