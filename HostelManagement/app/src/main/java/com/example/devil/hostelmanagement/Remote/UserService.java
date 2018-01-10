package com.example.devil.hostelmanagement.Remote;

import com.example.devil.hostelmanagement.model.ResObj;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by DEVIL on 12/26/2017.
 */

public interface UserService {

    @POST("login")
    Call<JSONObject> login(@Body JSONObject body);

    @POST("signup")
    Call<JSONObject> register(@Body JSONObject body);

    @GET("fooddata")
    Call<JsonObject> fooddata(@Query("date") String date);

}
