package com.example.edvin.app.util;

import com.example.edvin.app.models.User;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BaseApiService {

    @GET("users")
    Call<List<User>> getUsers();

    @POST("users/post")
    Call<User> createUser(@Body User user);


  @PUT("users/put")
  Call<User> putUser(@Body User user);
  //TODO check med edvinboiii

}
