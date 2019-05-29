package com.example.edvin.app.util;

import com.example.edvin.app.models.Challenge;
import com.example.edvin.app.models.Material;
import com.example.edvin.app.models.Position;
import com.example.edvin.app.models.Report;
import com.example.edvin.app.models.Station;
import com.example.edvin.app.models.User;
import com.example.edvin.app.models.UserAccount;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BaseApiService {

   @GET("users")
    Call<List<User>> getUsers();


    @GET("users/authenticate/{myPath}")
    Call<User> getAuthenticated(
            @Path("myPath") String path
    );

    @GET ("/useraccounts/{id}")
    Call<UserAccount> getUserAccount(@Path("id") int id);

    @GET("reports/{station}")
    Call<List<Report>> getReportsForStation(@Path("station") String station);

    @GET("positions")
    Call<List<Position>> getPositions();

    @GET("stations")
    Call<List<Station>> getStations();

    @GET("materials")
    Call<List<Material>> getMaterials();

    @POST("users/post")
    Call<User> createUser(@Body User user);

    @POST("reports/post")
    Call<Void> postReport (@Body Report report);

    @PUT("users/put")
    Call<User> putUser(@Body User user);

    @GET("challenges")
    Call<Challenge> getChallenges();

    @GET("reports")
    Call<List<Report>> getReports();

}
