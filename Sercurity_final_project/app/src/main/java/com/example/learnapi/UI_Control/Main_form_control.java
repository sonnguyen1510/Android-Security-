package com.example.IrisREC.UI_Control;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.IrisREC.Data.APIInterface.Admin_Interface;
import com.example.IrisREC.Data.APIService.RetrofitClient;
import com.example.IrisREC.Object.Admin;
import com.example.IrisREC.R;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main_form_control extends AppCompatActivity {


    private Admin admin;
    public TextView AdminName;
    public DrawerLayout drawerLayout;
    public Activity context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_form);
        String AdminID = getIntent().getStringExtra("Admin Id");
        Log.e("Admin id" , AdminID);
        AdminName = findViewById(R.id.Admin_name);

        //AdminName = findViewById(R.id.Admin_name);
        drawerLayout = findViewById(R.id.drawer_layout);
        //---------------------------NAVIGATION DRAWER----------------------------
        ImageView main_form_Menu;


        //menu
        findViewById(R.id.main_form_Menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });
        //back to home
        findViewById(R.id.main_form_BackToHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        //add User
        Class AddUser_Activity = Add_user_Control.class;

        findViewById(R.id.main_form_AddUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AddUser = new Intent(view.getContext(),AddUser_Activity);
                AddUser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                AddUser.putExtra("Admin Id",admin.get_id());
                view.getContext().startActivity(AddUser);
            }
        });

        //logOut
        Class LoginForm = Login_Controll.class;
        String Message = "LogOut";

        findViewById(R.id.main_form_BackToLogInForm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder Confirm = new AlertDialog.Builder(view.getContext());
                Confirm.setTitle("Log Out")
                        .setMessage("Are you sure ?")
                        .setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent BackToLogin = new Intent(view.getContext(),LoginForm);
                                BackToLogin.putExtra("LogOut",Message);
                                context.finish();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .create();

            }
        });



        //GetAdminInformation
        Admin_Interface admin_interface = RetrofitClient.getRetrofit().create(Admin_Interface.class);
        Call<Admin> CallAdminAPI = admin_interface.GetAdminByID(AdminID);
        CallAdminAPI.enqueue(new Callback<Admin>() {
            @Override
            public void onResponse(Call<Admin> call, Response<Admin> response) {
                admin = response.body();
                if(admin == null){
                    Log.e("Call APi","Get Admin information fail");
                }
                else{
                    AdminName.setText(admin.getName()+"");
                }
            }

            @Override
            public void onFailure(Call<Admin> call, Throwable t) {
                Log.e("Call Api" , "Get admin information fail");
            }
        });

    }


    private static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private static void CloseDrawer(DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        CloseDrawer(drawerLayout);
    }
}