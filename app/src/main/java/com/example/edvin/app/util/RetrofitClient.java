package com.example.edvin.app.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    /********
     * URLS
     *******/
    private static final String ROOT_URL = "https://pvtgrupp06.herokuapp.com/";

    /**
     * Get Retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {

        return new Retrofit.Builder()
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
