package com.example.learnapi.API.APIInterface;

import com.example.learnapi.Object.Admin;
import com.example.learnapi.Object.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface    User_Interface {
    //http://localhost:3000/v1/User/get_all_user

    //getUser
    @GET("api/v1/User/get_all_user")
    Call<List<User>> getUserData ();

    @GET("api/v1/User/get_user/{id}")
    Call<User> getUserByID(@Path("id") String id);

    //AddUser
}

