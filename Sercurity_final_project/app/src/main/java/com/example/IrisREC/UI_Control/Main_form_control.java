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
import com.example.IrisREC.Data.APIInterface.User_Interface;
import com.example.IrisREC.Data.APIService.AdminCall;
import com.example.IrisREC.Data.APIService.RetrofitClient;
import com.example.IrisREC.Data.APIService.UserCall;
import com.example.IrisREC.Object.Admin;
import com.example.IrisREC.Object.User;
import com.example.IrisREC.R;
import com.example.IrisREC.View.UserList.View.UserList_RecycleView_Adapter;
import com.example.IrisREC.View.UserList.ObjectView.RecyclerTouchListener;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main_form_control extends AppCompatActivity {


    private Admin admin;
    public TextView AdminName;
    public DrawerLayout drawerLayout;
    public Activity context = this;
    public static String AdminID;
    //User list
    public RecyclerView User_list;
    public List<User> UserData;
    public UserList_RecycleView_Adapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_form);
        AdminID = getIntent().getStringExtra("Admin Id");
        //Log.e("Admin id" , AdminID);
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
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Log Out")
                        .setMessage("Are you sure ?")
                        .setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent BackToLogin = new Intent(view.getContext(),LoginForm);
                                BackToLogin.putExtra("LogOut",Message);
                                view.getContext().startActivity(BackToLogin);
                                context.finish();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .create().show();

            }
        });



        //GetAdminInformation
        AdminCall.getAdminByID(AdminID).enqueue(new Callback<Admin>() {
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

        //---------------------------------------SHOW LIST USER---------------------------
        User_list = findViewById(R.id.User_List);
        //Get User List
        UserCall.GetAllUserData().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                UserData = response.body();
                adapter = new UserList_RecycleView_Adapter(UserData,context);
                User_list.setAdapter(adapter);
                User_list.setLayoutManager(new LinearLayoutManager(context));
                User_list.addOnItemTouchListener(new RecyclerTouchListener(context, User_list, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        Bundle bundle = new Bundle();
                        bundle.putString("User Id",UserData.get(position).get_id());
                        bundle.putString("Admin Id", AdminID);

                        Intent UserInformation = new Intent(context,UserInformation_Control.class);
                        UserInformation.putExtras(bundle);
                        startActivity(UserInformation);

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("APICall","Get List User Fail");
            }
        });
        //Upload User data to recycle view
        /*
        *
        * */

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