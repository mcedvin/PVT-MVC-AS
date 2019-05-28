package com.example.edvin.app.util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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



    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder()

            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .build();
    /**
     * Get Retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
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
