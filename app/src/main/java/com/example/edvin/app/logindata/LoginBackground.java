package com.example.edvin.app.logindata;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

public class LoginBackground extends AsyncTask<String,Void,String> {
    Context context;

    AlertDialog alert;

    public LoginBackground(Context ctx){
        context = ctx;
    }


    @Override
    protected String doInBackground(String... params) {
        if(!checkInputs())
            return "Bad input";
        else {

            String type = params[0];


            /**
             * signing in
             */
            if (type.equalsIgnoreCase("signin")) {
                if (params[4].equals(params[3]))
                    return "Hello, " + params[1] + ", your account is being added";
                else
                    return "Not matching passwords";


                /**
                 * Logging in
                 */
            } else if(type.equalsIgnoreCase("login")) {

                return "Checking the database";
            }

        }
        return "";
    }

    @Override
    protected void onPreExecute() {
        alert = new AlertDialog.Builder(context).create();
        alert.setTitle("Sign in Status");
    }


    @Override
    protected void onPostExecute(String result) {
        alert.setMessage(result);

        alert.show();
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    private boolean checkInputs(String... params) {
        for (String s : params){
            if (s.length()==0)
                return false;
        }

        return true;
    }


}