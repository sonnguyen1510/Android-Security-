package com.example.learnapi.UI_Control;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.learnapi.Data.APIInterface.Admin_Interface;
import com.example.learnapi.Data.APIService.RetrofitClient;
import com.example.learnapi.Object.Admin;
import com.example.learnapi.R;

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


                Admin_Interface admin_interface = RetrofitClient.getRetrofit().create(Admin_Interface.class);
                Call<Admin> callAdminAPI = admin_interface.AdminCheck(UserName.getText().toString(),Password.getText().toString());
                callAdminAPI.enqueue(new Callback<Admin>() {
                    @Override
                    public void onResponse( Call<Admin> call, Response<Admin> response) {
                        Admin admin_check = response.body();

                        if(admin_check == null){
                            //----------------------------------------POPUP SETTING----------------------------------------
                            //Show popup window when login fail
                            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                            View LoginFailPopUp = inflater.inflate(R.layout.login_popup_window,null);

                            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                            int Height = LinearLayout.LayoutParams.WRAP_CONTENT;
                            boolean Focusable = true;

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
                            goToAdminForm.putExtra("From_LogInForm",admin_check.get_id());
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
    }
}
