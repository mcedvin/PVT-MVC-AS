package com.example.edvin.app.util;

import com.example.edvin.app.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BaseApiService {

    @GET("users")
    Call<List<User>> getUsers();

}
