package com.example.IrisREC.UI_Control;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.IrisREC.Data.APIService.UserCall;
import com.example.IrisREC.Function.FunctionImplement;
import com.example.IrisREC.Function.IrisFunctionImplement;
import com.example.IrisREC.Object.User;
import com.example.IrisREC.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Check_iris_recognition_Control extends AppCompatActivity {

    private Mat frameIn;
    private Mat frameOut;
    private Mat eyeCircleSelection;
    public static Mat JNIReturn;
    private Mat JNIReturnNormalized;
    private int framesPassed;
    private Vector<Integer> irisCodeInput = new Vector<>();
    private boolean IRISRECOGNITION = false;
    private static String TAG = "AuthenticateActivity";
    private static JavaCameraView jcv;
    private int [] histogramValues = {0, 1, 2, 3, 4, 6, 7, 8, 12, 14, 15, 16, 24, 28, 30, 31, 32, 48, 56, 60, 62, 63, 64, 96, 112, 120, 124, 126, 127, 128, 129, 131, 135, 143, 159, 191, 192, 193, 195, 199, 207, 223, 224, 225, 227, 231, 239, 240, 241, 243, 247, 248, 249, 251, 252, 253, 254, 255};
    public final static int IRISRECOGNITION_LOADIMG_REQUESTCODE = 323;

    public Bitmap EyeInputData;
    public List<User> UserData;
    public Button LoadImage;
    public ImageView IrisLocalization;
    public ImageView IrisNomalization;
    public TextView Back_To_Login;
    public String UserID;

    BaseLoaderCallback mLoader = new BaseLoaderCallback(this)
    {
        @Override
        public void onManagerConnected(int status)
        {
            switch (status)
            {
                case BaseLoaderCallback.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");

                    break;
                }
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

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

    //The JNI link to the C++ function
    //public native void detectIris(long addrInput, long addrOutput, long addrOutputNormalized, long addrOriginal);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_iris_recognition_form);

        //Load image load button
        LoadImage = findViewById(R.id.iris_check_loadimage);
        IrisNomalization = findViewById(R.id.IrisCrop);
        IrisLocalization = findViewById(R.id.LoadImg);
        Back_To_Login = findViewById(R.id.Back_To_Login);

        //Custom layout
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        UserCall.GetAllUserData().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
            UserData = response.body();
            Log.e("User Call", "Get User success");
        }

        @Override
        public void onFailure(Call<List<User>> call, Throwable t) {
            Log.e("User Call", "Get User fail");
        }
    });

        //load  openCv library
        //Loader.load(opencv_java.class);

        if (OpenCVLoader.initDebug())
        {
            Log.i(TAG, "OpenCV loaded successfully.");
            mLoader.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else
        {
            Log.i(TAG, "OpenCV load failed.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoader);
        }
        Back_To_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent BackToLogin = new Intent(view.getContext(),Login_Controll.class);
                BackToLogin.putExtra("LogOut","123");
                view.getContext().startActivity(BackToLogin);
            }
        });


        LoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GetImage = new Intent(Intent.ACTION_PICK);
                GetImage.setType("image/*");
                startActivityForResult(GetImage,IRISRECOGNITION_LOADIMG_REQUESTCODE);
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mLoader);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                //Load IMG from capture
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                EyeInputData =FunctionImplement.ARGBBitmap(BitmapFactory.decodeStream(imageStream));



                //Iris Localization
                Bitmap Localization = IrisFunctionImplement.IrisLocalization(EyeInputData);
                IrisLocalization.setImageBitmap(Localization);

                //Iris Normalization
                Bitmap Normalization = IrisFunctionImplement.IrisNormalization(EyeInputData);
                IrisNomalization.setImageBitmap(Normalization);

                //Decode Iris
                Mat NormalizationMat = new Mat(Normalization.getWidth(), Normalization.getHeight(), CvType.CV_8UC1);
                Utils.bitmapToMat(Normalization,NormalizationMat);
                Vector<Integer> Encode = IrisFunctionImplement.Encode(NormalizationMat);

                boolean IsUserExist = false;
                //Check iris
                for(User UserCheck : UserData){
                    if(
                            IrisFunctionImplement.hammingDistance(
                                    FunctionImplement.ConvertListToVector(UserCheck.getImageEyeCode()),
                                    FunctionImplement.ConvertListToVector(Encode)
                            )<0.1
                    ){
                        //Log.e("User name",UserCheck.getName());
                        UserID = UserCheck.get_id();
                        IsUserExist = true;
                        Intent ShowUserInformation = new Intent(this, Show_UserInformation_Control.class);
                        ShowUserInformation.putExtra("UserIdIris",UserID);
                        startActivity(ShowUserInformation);
                    }
                }

                if(!IsUserExist){
                    new AlertDialog.Builder(this)
                            .setTitle("Iris Recognition check")
                            .setMessage("This user doesn't exist")
                            .setNegativeButton("Again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .create().show();
                }



            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            Intent BackToLogin = new Intent(this,Login_Controll.class);
            BackToLogin.putExtra("LogOut","123");
            startActivity(BackToLogin);
            finish();
        }
        return true;
    }

}