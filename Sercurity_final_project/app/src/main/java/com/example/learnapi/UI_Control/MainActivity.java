package com.example.IrisREC.UI_Control;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.IrisREC.Data.APIInterface.User_Interface;
import com.example.IrisREC.Data.APIService.RetrofitClient;
import com.example.IrisREC.Object.User;
import com.example.IrisREC.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public Button CallAPI;
    public TextView Name;
    public TextView ID;
    public ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        //https://data.mongodb-api.com/app/data-lpfsm/endpoint/data/beta

        CallAPI = findViewById(R.id.CallAPI);
        Name = findViewById(R.id.OP_name);
        ID = findViewById(R.id.OP_ID);
        imageView = findViewById(R.id.OP_image);



        CallAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User_Interface api_service = RetrofitClient.getRetrofit().create(User_Interface.class);
                Call<User> APICall = api_service.getUserByID("6272a26c1170c40bb8f284a3");
                APICall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User Result = response.body();
                        Name.setText(Result.getName()+"");
                        ID.setText(Result.get_id()+"");

                        imageView.setImageBitmap(ConvertStringBase64ToBitMap(Result.getImageEye()));

                        Log.e("Success","Ngon");
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        });



        /*
        * CallAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                API_service.api_service.getUserByID("6272a26c1170c40bb8f284a3").enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_LONG);
                        Log.e("Success","Ngon");

                        User Result = response.body();

                        Name.setText(Result.getUser_Name()+"");
                        ID.setText(Result.get_id()+"");
                        Log.e("Success","Ngon");

                        //Log.e("Result",Result.getName()+" "+Result.getPhone());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(MainActivity.this,"Fail",Toast.LENGTH_LONG);
                        Log.e("Fail","Fail vcl");
                    }
                });
            }
        });
        * */

    }
    public Bitmap ConvertStringBase64ToBitMap(String imageString){
        byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedByte;
    }
}