package com.example.edvin.app.util;

import com.example.edvin.app.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface BaseApiService {

    @GET("users")
    Call<List<User>> getUsers();

    @POST("users")
    Call<User> createUser(@Body User user);


  @PUT("users")
  Call<User> putUser(String email,@Body User user);
  //TODO check med edvinboiii

}
