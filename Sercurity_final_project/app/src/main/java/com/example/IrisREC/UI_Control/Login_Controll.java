package com.example.IrisREC.UI_Control;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.IrisREC.Data.APIInterface.Admin_Interface;
import com.example.IrisREC.Data.APIService.AdminCall;
import com.example.IrisREC.Data.APIService.RetrofitClient;
import com.example.IrisREC.Object.Admin;
import com.example.IrisREC.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login_Controll extends AppCompatActivity {
    public EditText Password;
    public EditText UserName;
    public Button LogIn;
    public ImageView Iris;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_form);

        Password = findViewById(R.id.Password_input);
        UserName = findViewById(R.id.UserName_input);
        LogIn = findViewById(R.id.Log_In);
        Iris = findViewById(R.id.Log_In_with_Iris);

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log In Check
                String IsLogInMoreThanOne = getIntent().getStringExtra("LogOut");
                if(!(IsLogInMoreThanOne == null)){
                    UserName.setText("");
                    Password.setText("");
                }


                AdminCall.AdminCheck(
                        UserName.getText().toString(),
                        Password.getText().toString()
                ).enqueue(new Callback<Admin>() {
                    @Override
                    public void onResponse( Call<Admin> call, Response<Admin> response) {
                        Admin admin_check = response.body();

                        if(admin_check == null){
                            //----------------------------------------POPUP SETTING----------------------------------------
                            //Show popup window when login fail
                            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                            View LoginFailPopUp = inflater.inflate(R.layout.login_popup_window,(ViewGroup) findViewById(R.id.Login_Form));

                            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                            int Height = LinearLayout.LayoutParams.WRAP_CONTENT;
                            boolean Focusable = false;

                            final PopupWindow LoginFailPopUpWindow = new PopupWindow(LoginFailPopUp,width,Height,Focusable);
                            LoginFailPopUpWindow.showAtLocation(view, Gravity.CENTER,0,0);

                            //button click to dismiss popup window
                            Button Login_popup_window_button = LoginFailPopUp.findViewById(R.id.Login_popup_window_button);
                            Login_popup_window_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    LoginFailPopUpWindow.dismiss();
                                }
                            });
                        }
                        else{
                            Intent goToAdminForm = new Intent(view.getContext(),Main_form_control.class);
                            goToAdminForm.putExtra("Admin Id",admin_check.get_id());
                            view.getContext().startActivity(goToAdminForm);
                        }
                    }

                    @Override
                    public void onFailure(Call<Admin> call, Throwable t) {
                        Log.e("Call Admin Api","Admin check fail");
                    }
                });
            }
        });

        //----------------------------------IRIS RECOGNITION---------------------------------
        Class IrisCheck = Check_iris_recognition_Control.class;
        Iris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DirectToCheck_Iris = new Intent(view.getContext(),IrisCheck);
                String Message = "Request_Iris_Check";
                DirectToCheck_Iris.putExtra("IrisCheck",Message);
                view.getContext().startActivity(DirectToCheck_Iris);
            }
        });
    }
}
