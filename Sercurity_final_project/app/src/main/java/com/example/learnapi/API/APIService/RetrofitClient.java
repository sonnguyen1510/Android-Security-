package com.example.learnapi.API.APIService;

import com.example.learnapi.API.Network.Connection;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static Retrofit retrofit;
    public static Connection connection = new Connection();

    public static Retrofit getRetrofit() {
        connection.CreateLocalConnection("192.168.0.121","3000");//your computer's localhost
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(connection.BaseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }
}
