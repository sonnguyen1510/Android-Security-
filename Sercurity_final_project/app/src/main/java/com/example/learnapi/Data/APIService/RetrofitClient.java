package com.example.IrisREC.Data.APIService;

import com.example.IrisREC.Data.Network.CreateAPILink;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static Retrofit retrofit;
    public static CreateAPILink connection = new CreateAPILink();

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
