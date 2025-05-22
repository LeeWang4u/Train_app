package com.example.train_app.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MomoRetrofit {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.88.59:5000/"; // Use 10.0.2.2 for localhost on Android emulator

    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
