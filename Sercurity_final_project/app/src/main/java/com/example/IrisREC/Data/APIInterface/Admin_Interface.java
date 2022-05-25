package com.example.IrisREC.Data.APIInterface;

import com.example.IrisREC.Object.Admin;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Admin_Interface {

    //LoginCheck
    /**
     * @apiNote http://localhost:{port}/api/v1/admin/Check_admin/UserName/Password
     * */
    @GET("api/v1/admin/Check_admin/{User_name}/{Password}")
    Call<Admin> AdminCheck(@Path("User_name") String User_name , @Path("Password") String Password);

    //get Admin by ID
    /**
     * @apiNote http://localhost:{port}/api/v1/admin/Check_admin/Get_admin/AdminID
     * */
    @GET("api/v1/admin/Get_admin/{AdminID}")
    Call<Admin> GetAdminByID(@Path("AdminID") String AdminID);
}
