package com.example.IrisREC.UI_Control;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
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
import com.example.IrisREC.Object.User;
import com.example.IrisREC.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_user_Control extends AppCompatActivity {

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
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        Birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(view.getContext(),date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
                    User_Interface user_interface = RetrofitClient.getRetrofit().create(User_Interface.class);
                    Call<User> CallUserAPI = user_interface.AddNewUser(new User(
                       null,
                            Name.getText().toString(),
                            Age.getText().toString(),
                            Birthday.getText().toString(),
                            Email.getText().toString(),
                            Gender(),
                            ImageEyeStringBase64(ImageEyeData),
                            0
                    ));
                    CallUserAPI.enqueue(new Callback<User>() {
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

    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);

        Birthday.setText(dateFormat.format(myCalendar.getTime()));
    }

    private String ImageEyeStringBase64(Bitmap data) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        data.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private boolean Gender() {
        if(IsFemale.isChecked()){
            return false;
        }
        else
            return true;
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
}