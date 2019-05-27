package com.example.edvin.app.util;

import com.example.edvin.app.models.Challenge;
import com.example.edvin.app.models.Material;
import com.example.edvin.app.models.Position;
import com.example.edvin.app.models.Report;
import com.example.edvin.app.models.Station;
import com.example.edvin.app.models.User;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {

    @GET("users")
    Call<List<User>> getUsers();

    @GET("positions")
    Call<List<Position>> getPositions();

    @GET("stations")
    Call<List<Station>> getStations();

    @GET("materials")
    Call<List<Material>> getMaterials();

    @POST("users/post")
    Call<User> createUser(@Body User user);

    @PUT("users/put")
    Call<User> putUser(@Body User user);

    @GET("challenges")
    Call<Challenge> getChallenges();

/**/
/* returns current reports for specified station
/**/
    @GET("reports/{stationName}")
    Call<List<Report>> getReportsForStation(@Path("stationName") String stationName);

    @GET("reports")
    Call<List<Report>> getReports();

}
