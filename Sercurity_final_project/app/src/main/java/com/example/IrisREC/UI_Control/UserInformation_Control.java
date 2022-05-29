
package com.example.IrisREC.UI_Control;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.IrisREC.Data.APIService.UserCall;
import com.example.IrisREC.Function.FunctionImplement;
import com.example.IrisREC.Object.User;
import com.example.IrisREC.R;

import org.opencv.android.OpenCVLoader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInformation_Control extends AppCompatActivity {

    public TextView Name;
    public TextView Email;
    public TextView Birthday;
    public TextView Age;
    public TextView Gender;
    public ImageView ImageEye;

    public Button Delete;
    public Button Disable;
    public static String  UserID;
    public static String AdminId;
    public User UserData;

    static
    {
        if (OpenCVLoader.initDebug())
            Log.i(TAG, "OpenCV loaded successfully.");
        else
            Log.i(TAG, "OpenCV load failed.");
    }

    //Loads opencv and nativelib
    static
    {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_user_form);

        //get data
        Bundle getData = this.getIntent().getExtras();
        UserID = getData.getString("User Id");
        AdminId = getData.getString("Admin Id");

        //Set view

        Name = findViewById(R.id.User_name);
        Email = findViewById(R.id.UserEmail);
        Birthday = findViewById(R.id.User_Birthday);
        Age = findViewById(R.id.User_Age);
        ImageEye = findViewById(R.id.User_imageEye);
        Gender = findViewById(R.id.User_Gender);

        //custom layout
        findViewById(R.id.IrisInformation).setVisibility(View.GONE);
        findViewById(R.id.Show_user_form_button).setVisibility(View.VISIBLE);

        //Get User
        UserCall.GetUserByID(UserID).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                UserData = response.body();

                Name.setText(UserData.getName());
                Email.setText(UserData.getEmail()+"");
                Birthday.setText(UserData.getBirthday()+"");
                Age.setText(UserData.getAge()+"");
                if(UserData.isGender()){
                    Gender.setText("Female");
                }
                else
                    Gender.setText("Male");

                ImageEye.setImageBitmap(FunctionImplement.ConvertStringBase64ToBitMap(UserData.getImageEye()));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Show user", "load user information fail");
            }
        });



        //Delete user
        findViewById(R.id.DeleteUser_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete user")
                        .setMessage("Do you want to delete this user ?")
                        .setIcon(R.drawable.ic_delete)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Log.e("User_ID" , UserID);
                                UserCall.Delete(UserID).enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        new AlertDialog.Builder(view.getContext())
                                                .setTitle("Delete User")
                                                .setMessage("Delete user successful!")
                                                .setIcon(R.drawable.ic_success)
                                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent BackToHome = new Intent(view.getContext(),Main_form_control.class);
                                                        BackToHome.putExtra("Admin Id",AdminId);
                                                        view.getContext().startActivity(BackToHome);

                                                    }
                                                })
                                                .create().show();
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        new AlertDialog.Builder(view.getContext())
                                                .setTitle("Delete User")
                                                .setMessage("Delete user fail!")
                                                .setIcon(R.drawable.ic_fail)
                                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent BackToHome = new Intent(view.getContext(),Main_form_control.class);
                                                        BackToHome.putExtra("Admin Id",AdminId);
                                                        view.getContext().startActivity(BackToHome);

                                                    }
                                                })
                                                .create().show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create().show();
            }
        });
    }
}