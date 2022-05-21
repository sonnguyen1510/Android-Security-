package com.example.IrisREC.Data.APIInterface;

import com.example.IrisREC.Object.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    //Delete User
    @DELETE("api/v1/User/delete/{Id}")
    Call<User> DeleteAUser(@Path("Id") String id);
}

