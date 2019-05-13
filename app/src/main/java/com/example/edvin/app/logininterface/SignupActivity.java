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
            final String fname = fnameTxt.getText().toString();
            final String lname = lnameTxt.getText().toString();
            final String mail = mailTxt.getText().toString();
            final String pass1 = lnameTxt.getText().toString();
            final String pass2 = lnameTxt.getText().toString();


            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(SignupActivity.this);
            dialog.setTitle("Getting JSON data");
            dialog.setMessage("Please wait...");
            dialog.show();



            //Creating an object of our api interface
            BaseApiService api = RetrofitClient.getApiService();

            User newUser = new User(fname,lname,mail,pass1);


            /**
             * Calling JSON
             */
            Call<User> call = api.createUser(newUser);


            /**
             * Enqueue Callback will be call when get response...
             */

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {


                    dialog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(SignupActivity.this,"is not successful, Code: " + response.code(),Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SignupActivity.this,"OnFailure: "+t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
