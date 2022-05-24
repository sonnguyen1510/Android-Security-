/*

package com.example.IrisREC.UI_Control;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.IrisREC.Data.APIService.UserCall;
import com.example.IrisREC.Object.User;
import com.example.IrisREC.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserIFormation_Control extends AppCompatActivity {

    public TextView Name;
    public TextView Email;
    public TextView Birthday;
    public TextView Age;
    public Button Delete;
    public Button Disable;
    public static String  UserID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.show_user_form);

        UserID = getIntent().getStringExtra("UserID");
        Name = findViewById(R.id.User_name);
        Email = findViewById(R.id.UserEmail);
        Birthday = findViewById(R.id.User_Birthday);
        Age = findViewById(R.id.User_Age);

        //Get User
        UserCall.GetUserByID(UserID).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });



        UserCall.Delete(UserID).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}

* */