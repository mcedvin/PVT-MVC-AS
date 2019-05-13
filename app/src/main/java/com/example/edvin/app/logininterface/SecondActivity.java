package com.example.edvin.app.logininterface;


import android.app.ProgressDialog;
import android.content.Intent;
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

import com.example.edvin.app.models.InternetConnection;
import com.example.edvin.app.models.User;
import com.example.edvin.app.util.BaseApiService;
import com.example.edvin.app.util.RetrofitClient;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import com.example.edvin.app.logindata.LoginBackground;
import com.example.edvin.app.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondActivity extends AppCompatActivity {

    EditText passTxt;
    EditText userTxt;
    Button startBtn;


    CallbackManager callbackManager;
    TextView txtEmail, txtBirthday, txtFriends;
    ProgressDialog mDialog;
    ImageView imgAvatar;

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

        callbackManager = CallbackManager.Factory.create();
        txtBirthday = (TextView) findViewById(R.id.txtBirthday);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtFriends = (TextView) findViewById(R.id.txtFriends);

        imgAvatar = (ImageView) findViewById(R.id.avatar);

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
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
                        getData(object);
                    }
                });

                //Request Graph API
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,birthday,friends");
                request.setParameters(parameters);
                request.executeAsync();

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


    public void onClick(View view) {


        /*String type = "login";

        String username = userTxt.getText().toString();
        String password = passTxt.getText().toString();


        LoginBackground bgw = new LoginBackground(this);
        bgw.execute(type, username, password);*/

        if(InternetConnection.checkConnection(getApplicationContext())){

            final ProgressDialog dialog;
            final String username = userTxt.getText().toString();
            final String password = passTxt.getText().toString();

            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(SecondActivity.this);
            dialog.setTitle("Getting JSON data");
            dialog.setMessage("Please wait...");
            dialog.show();



            //Creating an object of our api interface
            BaseApiService api = RetrofitClient.getApiService();


            /**
             * Calling JSON
             */
            Call<List<User>> call = api.getUsers();

            /**
             * Enqueue Callback will be call when get response...
             */

            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                    dialog.dismiss();
                    if (!response.isSuccessful()) {
                        Toast.makeText(SecondActivity.this,"Code: " + response.code(),Toast.LENGTH_SHORT).show();
                        return;
                    }
                    /**
                     * Got Successfully
                     */

                    List<User> users = response.body();
                    Toast.makeText(SecondActivity.this,checkUsers(users,username,password),Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(SecondActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });



        }


    }


    protected String checkUsers(List<User> users, String usertext, String p){

        for (User u : users){
            if(usertext.equals(u.getEmail())){
                if( u.getUserAccount() != null){
                    if(u.getUserAccount().getPassword().equals(p)){
                        return "Hello, "+u.getFirstName()+"! Logging in into your account..";
                    }else
                        return "wrong password";
               }else
                    return "NULL ACCOUNT";
            }

        }

        return "EMAIL NOT FOUND";


    }



}