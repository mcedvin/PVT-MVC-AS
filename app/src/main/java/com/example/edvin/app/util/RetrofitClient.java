package com.example.edvin.app.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    /********
     * URLS
     *******/
    private static final String ROOT_URL = "https://recycling-rest.herokuapp.com/";


    /****
     * HTTP Client
     *
     ****/
    //    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
//
//            .connectTimeout(30, TimeUnit.SECONDS)
//            .readTimeout(40, TimeUnit.SECONDS)
//            .writeTimeout(40, TimeUnit.SECONDS)
//            .build();


    /*private static Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setDateFormat(DateFormat.DATE_FIELD)
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .setVersion(1.0).
            .create();*/


    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create();


    /**
     * Get Retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {

        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .client(new OkHttpClient().newBuilder()
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .readTimeout(40, TimeUnit.SECONDS)
                        .writeTimeout(40, TimeUnit.SECONDS)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


    }

    /**
     * Get API Service
     *
     * @return API Service
     */
    public static BaseApiService getApiService() {
        return getRetrofitInstance().create(BaseApiService.class);
    }


}
