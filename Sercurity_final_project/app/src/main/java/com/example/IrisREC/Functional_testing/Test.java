package com.example.IrisREC.Functional_testing;

import static com.example.IrisREC.UI_Control.Check_iris_recognition_Control.IRISRECOGNITION_LOADIMG_REQUESTCODE;
//import static com.example.IrisREC.UI_Control.Check_iris_recognition_Control.JNIReturn;

import static org.opencv.imgproc.Imgproc.circle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.IrisREC.Function.FunctionImplement;
import com.example.IrisREC.Function.IrisFunctionImplement;
import com.example.IrisREC.Function.NativeFunctionCall_IrisFunction;
import com.example.IrisREC.R;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Vector;


public class Test extends AppCompatActivity {

    public Bitmap EyeInput ;
    public static Mat JNIReturn;
    private Mat JNIReturnNormalized;
    private int framesPassed;

    private Mat eyeCircleSelection ;

    private Mat InputData;
    private Mat OutputData;
    private Button LoadImg;
    private ImageView IrisCrop;
    private static final String TAG = "MainActivity";

    static {
        if(!OpenCVLoader.initDebug()){
            Log.d(TAG, "OpenCV not loaded");
        } else {
            Log.d(TAG, "OpenCV loaded");
        }
    }

    //Loads opencv and nativelib
    static
    {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
    }

    //public native void detectIris(long addrInput, long addrOutput, long addrOutputNormalized, long addrOriginal);



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);

        OpenCVLoader.initDebug();
        JNIReturn = new Mat();
        JNIReturnNormalized = new Mat();
        eyeCircleSelection =new Mat();
        OutputData = new Mat();


        LoadImg = findViewById(R.id.TestIris);
        IrisCrop = findViewById(R.id.OP_image);

        LoadImg.setOnClickListener(new View.OnClickListener() {
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
        if(!OpenCVLoader.initDebug()){
            Log.d(TAG, "OpenCV not loaded");
        } else {
            Log.d(TAG, "OpenCV loaded");

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);

                Bitmap Eye_data =FunctionImplement.ARGBBitmap(BitmapFactory.decodeStream(imageStream));
                Mat input = FunctionImplement.ConvertBitmapToMat(Eye_data);
                Mat output = new Mat();
                NativeFunctionCall_IrisFunction.Segmentation(input.getNativeObjAddr(),output.getNativeObjAddr());

                Log.e("Encode",EncodeIM(IrisFunctionImplement.Encode(IrisFunctionImplement.IrisNormalization_ToMat(Eye_data))));

                IrisCrop.setImageBitmap(FunctionImplement.ConvertMatToBitmap(output));


                /*
                *

                //Converts input frame to Mat




                * */


                /*
                 //Crop the eye circle
                Bitmap EyeData = Iris_Function.Hough_transform(BitmapFactory.decodeStream(imageStream));

                LoadImg.setImageBitmap(EyeData);

                Mat Crop = new Mat();
                Utils.bitmapToMat(EyeData,Crop);

                Crop = Iris_Function.CropIrisImage(Crop);
                Bitmap AfCrop = Bitmap.createBitmap(Crop.cols(), Crop.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(Crop,AfCrop);
                IrisCrop.setImageBitmap(AfCrop);


                ImageEyeView.setImageBitmap(ImageEyeData);
                this.ImageEyeData = ImageEyeData;
                IsUploadedImage = true;
                * */
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }

    }

    private String EncodeIM(Vector<Integer> encode) {
        String Result="[";
        for(int Element : encode){
            Result += String.valueOf(Element)+",";
        }
        Result+= "]";

        return Result;
    }
}