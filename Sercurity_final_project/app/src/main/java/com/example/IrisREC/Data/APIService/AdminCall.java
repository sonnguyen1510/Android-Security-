package com.example.IrisREC.Data.APIService;

import com.example.IrisREC.Data.APIInterface.Admin_Interface;
import com.example.IrisREC.Object.Admin;

import retrofit2.Call;

public class AdminCall {
    public static Call<Admin> getAdminByID(String Id){
        Admin_Interface admin_interface = RetrofitClient.getRetrofit().create(Admin_Interface.class);
        return admin_interface.GetAdminByID(Id);
    }

    public static Call<Admin> AdminCheck(String username , String password){
        Admin_Interface admin_interface = RetrofitClient.getRetrofit().create(Admin_Interface.class);
        return admin_interface.AdminCheck(username, password);
    }
}
