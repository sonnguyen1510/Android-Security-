package com.example.IrisREC.UI_Control;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.IrisREC.Data.APIInterface.User_Interface;
import com.example.IrisREC.Data.APIService.RetrofitClient;
import com.example.IrisREC.Data.APIService.UserCall;
import com.example.IrisREC.Function.FunctionImplement;
import com.example.IrisREC.Function.IrisFunctionImplement;
import com.example.IrisREC.Object.User;
import com.example.IrisREC.R;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_user_Control extends AppCompatActivity  {

    public DrawerLayout drawerLayout;
    public Activity context = this;

    //
    public EditText Name;
    public EditText Email;
    public EditText Birthday;
    public EditText Age;
    public RadioButton IsMale;
    public RadioButton IsFemale;
    public TextView Chose_Eye_authentication;
    public Button SignIn;
    public boolean IsUploadedImage = false;
    public ImageView ImageEyeView;

    //
    public Calendar myCalendar;
    public Bitmap ImageEyeData;


    //Loads opencv
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user_form);
        String AdminID = getIntent().getStringExtra("Admin Id");
        Log.e("Admin id" , AdminID);
        TextView AdminName = findViewById(R.id.Admin_name);

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
        Class MainFormClass = Main_form_control.class;
        findViewById(R.id.main_form_BackToHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent BackToHome = new Intent(view.getContext(),MainFormClass);
                BackToHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                BackToHome.putExtra("Admin Id",AdminID);
                view.getContext().startActivity(BackToHome);
            }
        });

        //add User
        Class AddUser_Activity = null;

        findViewById(R.id.main_form_AddUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
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

        //--------------------------------ADD_USER_FORM------------------------------
         Name = findViewById(R.id.Name_input);
         Email = findViewById(R.id.Email_input);
         Birthday = findViewById(R.id.Birthday_input);
         Age = findViewById(R.id.Age_input);
         IsMale = findViewById(R.id.IsMale);
         IsFemale = findViewById(R.id.IsFemale);
         Chose_Eye_authentication = findViewById(R.id.Input_EyeImage);
         SignIn = findViewById(R.id.SignInButton);
         ImageEyeView = findViewById(R.id.add_user_imageEye);
         //setup

        //Show calendar


        Birthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    showDialog(1100);
                }
            }
        });
        //CHOSE IMAGE FROM gallery
        Chose_Eye_authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GetImage = new Intent(Intent.ACTION_PICK);
                GetImage.setType("image/*");
                startActivityForResult(GetImage,3);
            }
        });


        //-----------------------SIGN IN BUTTON SETTING--------------------------------
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder Result = new AlertDialog.Builder(view.getContext());
                if(IsFullInformation()){
                    UserCall.AddUser(new User(
                            null,
                            Name.getText().toString(),
                            Age.getText().toString(),
                            Birthday.getText().toString(),
                            Email.getText().toString(),
                            Gender(),
                            FunctionImplement.ConvertBitmapToStringBase64(ImageEyeData),
                            ImageEyeToCode(ImageEyeData),
                            0
                    )).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User IsUserCreated = response.body();
                            if(IsUserCreated != null){
                                Result.setTitle("Result")
                                        .setMessage("Add User Successful!")
                                        .setNegativeButton("Home", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent BackToHome = new Intent(view.getContext(),MainFormClass);
                                                BackToHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                BackToHome.putExtra("Admin Id",AdminID);
                                                view.getContext().startActivity(BackToHome);
                                            }
                                        })
                                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                ClearData();
                                                dialogInterface.cancel();
                                            }
                                        })
                                        .create().show();
                            }
                            else{
                                Result.setTitle("Result")
                                        .setMessage("Add User Fail!")
                                        .setNegativeButton("Home", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent BackToHome = new Intent(view.getContext(),MainFormClass);
                                                BackToHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                BackToHome.putExtra("Admin Id",AdminID);
                                                view.getContext().startActivity(BackToHome);
                                            }
                                        })
                                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        })
                                        .create().show();
                            }
                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("API","Add user fail");
                        }
                    });
                }else{
                    Log.e("Info","Complete info");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap ImageEyeData= BitmapFactory.decodeStream(imageStream);

                ImageEyeView.setImageBitmap(ImageEyeData);
                this.ImageEyeData = ImageEyeData;
                IsUploadedImage = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private void ClearData() {
        Name.setText("");
        Email.setText("");
        Age.setText("");
        Birthday.setText("");
        IsFemale.setChecked(false);
        IsMale.setChecked(false);
        IsUploadedImage = false;
    }



    private Vector<Integer> ImageEyeToCode(Bitmap inputEye){
        Mat GetNormalizeImage = IrisFunctionImplement.IrisNormalization_ToMat(inputEye);

        return IrisFunctionImplement.Encode(GetNormalizeImage);
    }



    private boolean Gender() {
        if(IsFemale.isChecked()){
            return true;
        }
        else
            return false;
    }

    private boolean IsFullInformation(){
        if((Name.getText().toString().equalsIgnoreCase("")||
                Email.getText().toString().equalsIgnoreCase("")||
                Birthday.getText().toString().equalsIgnoreCase(""))||
                (!IsMale.isChecked()  && !IsFemale.isChecked())||
                !(IsUploadedImage )

        ){
            return false;
        }
        else
            return true;
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

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener(){

        //callback received when the user sets the date in the datePickerDialog
        @Override
        public void onDateSet(DatePicker view, int yearSelected, int monthOfYear, int dayOfMonth) {
            int year=yearSelected;
            int month=monthOfYear;
            int day=dayOfMonth;
            // Set the selected Date on Text
            Birthday.setText(day+"/"+month+"/"+year);
        }
    };

    protected Dialog onCreateDialog(int id)
    {
        switch (id) {
            case 1100:
                return new DatePickerDialog(this, mDateSetListener, 2020, 15, 10);
            }
        return null;
    }
}