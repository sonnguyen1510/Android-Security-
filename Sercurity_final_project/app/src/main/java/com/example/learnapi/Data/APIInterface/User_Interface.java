package com.example.learnapi.Data.APIInterface;

import com.example.learnapi.Object.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface    User_Interface {
    //http://localhost:3000/v1/User/get_all_user

    //getUser
    @GET("api/v1/User/get_all_user")
    Call<List<User>> getUserData ();

    //http://localhost:3000/v1/User/get_user/:id
    @GET("api/v1/User/get_user/{id}")
    Call<User> getUserByID(@Path("id") String id);

    //AddUser
    @POST("api/v1/User/Add_User")
    Call<User> AddNewUser(@Body User user);
}

