package com.example.learnapi.API.APIInterface;

import com.example.learnapi.Object.Admin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Admin_Interface {

    //LoginCheck
    @GET("api/v1/admin/Check_admin/{User_name}/{Password}")
    Call<Admin> AdminCheck(@Path("User_name") String User_name , @Path("Password") String Password);

    //get Admin by ID
    @GET("api/v1/admin/Get_admin/{AdminID}")
    Call<Admin> GetAdminByID(@Path("AdminID") String AdminID);
}
