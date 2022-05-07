package com.example.learnapi.UI_Control;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.learnapi.API.APIInterface.Admin_Interface;
import com.example.learnapi.API.APIService.RetrofitClient;
import com.example.learnapi.Object.Admin;
import com.example.learnapi.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.learnapi.databinding.MainFormBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main_form_control extends AppCompatActivity {


    private Admin admin;
    public TextView AdminName;
    public DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String AdminID = getIntent().getStringExtra("AdminID");
        //AdminName = findViewById(R.id.Admin_name);
        drawerLayout = findViewById(R.id.drawer_layout);


        //GetAdminInformation
        Admin_Interface admin_interface = RetrofitClient.getRetrofit().create(Admin_Interface.class);
        Call<Admin> CallAdminAPI = admin_interface.GetAdminByID(AdminID);
        CallAdminAPI.enqueue(new Callback<Admin>() {
            @Override
            public void onResponse(Call<Admin> call, Response<Admin> response) {
                admin = response.body();
                AdminName.setText(admin.getName()+"");
            }

            @Override
            public void onFailure(Call<Admin> call, Throwable t) {
                Log.e("Call Api" , "Get admin information fail");
            }
        });



    }


}