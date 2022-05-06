package com.example.learnapi.API.APIInterface;

import com.example.learnapi.Object.Admin;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Admin_Interface {

    //LoginCheck
    @FormUrlEncoded
    @POST("")
    Call<Admin> AdminCheck(@Field("User_name") String User_name , @Field("Password") String Password);
}
