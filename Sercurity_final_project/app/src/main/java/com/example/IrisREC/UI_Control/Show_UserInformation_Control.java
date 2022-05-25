package com.example.IrisREC.UI_Control;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.IrisREC.Data.APIService.UserCall;
import com.example.IrisREC.Function.FunctionImplement;
import com.example.IrisREC.Function.IrisFunctionImplement;
import com.example.IrisREC.Object.User;
import com.example.IrisREC.R;

import org.opencv.android.OpenCVLoader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Show_UserInformation_Control extends AppCompatActivity {
    //Base information
    public TextView Name;
    public TextView Email;
    public TextView Birthday;
    public TextView Age;
    public TextView Gender;
    public ImageView ImageEye;
    //Edit button
    public Button Delete;
    public Button Disable;
    public static String  UserID;
    public User UserData;
    //Show iris formation
    public RelativeLayout Iris_Information;
    public LinearLayout EditButtons;
    //Show image information
    public ImageView Iris_Segmentation;
    public ImageView Iris_Normalization;
    public ImageView Iris_Canny;
    public ImageView Iris_Gaussian;
    public ImageView Iris_Detect;

    //Load opencv
    static
    {
        if (OpenCVLoader.initDebug())
            Log.i(TAG, "OpenCV loaded successfully.");
        else
            Log.i(TAG, "OpenCV load failed.");
    }

    //Loads opencv and native lib
    static
    {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_user_form);

        //User information
        Name = findViewById(R.id.User_name);
        Email = findViewById(R.id.UserEmail);
        Birthday = findViewById(R.id.User_Birthday);
        Age = findViewById(R.id.User_Age);
        ImageEye = findViewById(R.id.User_imageEye);
        Gender = findViewById(R.id.User_Gender);
        //Show iris information
        Iris_Information = findViewById(R.id.IrisInformation);
        EditButtons = findViewById(R.id.Show_user_form_button);
        //Image Iris
        Iris_Segmentation = findViewById(R.id.iris_information_Segmentation);
        Iris_Normalization = findViewById(R.id.iris_information_Normalization);
        Iris_Canny = findViewById(R.id.iris_information_Canny);
        Iris_Gaussian = findViewById(R.id.iris_information_GaussianBlur);
        Iris_Detect = findViewById(R.id.iris_information_Iris);
        //Get User data
        UserID = getIntent().getStringExtra("UserIdIris");
        //Log.e("UserIDIris",UserID);

        //Custom layout

        EditButtons.setVisibility(View.GONE);
        Iris_Information.setVisibility(View.VISIBLE);
        //------------------------------------SHOW INFORMATION---------------------------------------

        //UserID = getIntent().getStringExtra("");

        //Get User
        UserCall.GetUserByID(UserID).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                UserData= response.body();
                //----------------------------------------SHOW USER-------------------------------------
                //Information
                Name.setText(UserData.getName()+"");
                Email.setText(UserData.getEmail()+"");
                Birthday.setText(UserData.getBirthday()+"");
                Age.setText(UserData.getAge()+"");

                if(UserData.isGender()){
                    Gender.setText("Male");

                }
                else
                    Gender.setText("Female");
                Bitmap ShowImageEye = FunctionImplement.ConvertStringBase64ToBitMap(UserData.getImageEye());
                ImageEye.setImageBitmap(ShowImageEye);

                //Image
                //Localization
                Iris_Canny.setImageBitmap(IrisFunctionImplement.IrisCanny(ShowImageEye));
                Iris_Canny.setImageBitmap(IrisFunctionImplement.IrisGaussian(ShowImageEye));
                Iris_Detect.setImageBitmap(IrisFunctionImplement.findIris(ShowImageEye));
                //Segmentation
                Iris_Segmentation.setImageBitmap(IrisFunctionImplement.IrisLocalization(ShowImageEye));
                //Normalization
                Iris_Normalization.setImageBitmap(IrisFunctionImplement.IrisNormalization(ShowImageEye));


            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Show User","Fail");
            }
        });






        //Log.e("TAG",UserDataI.getName());




        /*

        * */



    }
}
