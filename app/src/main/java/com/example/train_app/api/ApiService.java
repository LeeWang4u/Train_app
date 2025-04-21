package com.example.train_app.api;


import com.example.train_app.model.Station;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("station/all")
    Call<List<Station>> getAllStations();

}