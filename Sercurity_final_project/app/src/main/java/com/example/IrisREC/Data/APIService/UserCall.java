package com.example.IrisREC.Data.APIService;

import com.example.IrisREC.Data.APIInterface.User_Interface;
import com.example.IrisREC.Object.User;

import java.util.List;

import kotlin.contracts.Returns;
import retrofit2.Call;

public class UserCall {
    public static Call<List<User>> GetAllUserData(){
        User_Interface user_interface = RetrofitClient.getRetrofit().create(User_Interface.class);
        return user_interface.getUserData();
    }

    public static Call<User> GetUserByID(String ID){
        User_Interface user_interface = RetrofitClient.getRetrofit().create(User_Interface.class);
        return user_interface.getUserByID(ID);
    }

    public static Call<User> AddUser(User user){
        User_Interface user_interface = RetrofitClient.getRetrofit().create(User_Interface.class);
        return user_interface.AddNewUser(user);
    }

    public static Call<User> Delete(String ID){
        User_Interface user_interface = RetrofitClient.getRetrofit().create(User_Interface.class);
        return user_interface.DeleteAUser(ID);
    }
}
