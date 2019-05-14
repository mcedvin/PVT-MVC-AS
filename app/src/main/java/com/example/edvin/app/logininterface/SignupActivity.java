package com.example.edvin.app.logininterface;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.edvin.app.R;
import com.example.edvin.app.models.InternetConnection;
import com.example.edvin.app.models.User;
import com.example.edvin.app.util.BaseApiService;
import com.example.edvin.app.util.RetrofitClient;

import java.util.List;
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
            BaseApiService api = RetrofitClient.getApiService();

            User newUser = new User(firstName,lastName,email,password,getRandomNumberInRange(300,400));


            /**
             * Calling JSON
             */
            Call<User> call = api.createUser(newUser);
            //Call<User> call = api.putUser(email,newUser);


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

                    User postResponse = response.body();
                    Toast.makeText(SignupActivity.this, "Success! "+ response.code()+", Welcome "+postResponse.getFirstName()+"!" ,Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(SignupActivity.this,"Failure! No response: "+t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }
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
