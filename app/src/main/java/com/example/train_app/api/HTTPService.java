package com.example.train_app.api;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HTTPService {


//    public static final String APP_PATH = "http://192.168.1.37:8080/api/";


    public static final String APP_PATH = "http://192.168.1.10:8080/api/";

    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(APP_PATH)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}