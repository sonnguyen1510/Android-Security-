package com.example.IrisREC.UI_Control;

import static android.content.ContentValues.TAG;

import static org.opencv.core.CvType.CV_8U;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import com.example.IrisREC.Data.APIInterface.User_Interface;
import com.example.IrisREC.Data.APIService.RetrofitClient;
import com.example.IrisREC.Data.APIService.UserCall;
import com.example.IrisREC.Function.FunctionImplement;
import com.example.IrisREC.Function.Iris_Function;
import com.example.IrisREC.Object.User;
import com.example.IrisREC.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Check_iris_recognition_Control extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

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

    public Bitmap EyeInput;
    public List<User> UserData;
    public Button LoadImage;
    public ImageView Result;

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
                    jcv.enableView();
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
        Result = findViewById(R.id.LoadImg);

        //load  openCv library
        if (OpenCVLoader.initDebug())
        {
            Log.i(TAG, "OpenCV loaded successfully.");
            mLoader.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else
        {
            Log.i(TAG, "OpenCV load failed.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoader);
        }

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
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                EyeInput= FunctionImplement.ARGBBitmap(BitmapFactory.decodeStream(imageStream));




                Bitmap Result2 = Iris_Function.Hough_transform(EyeInput);
                Result.setImageBitmap(Result2);



                /*
                ImageEyeView.setImageBitmap(ImageEyeData);
                this.ImageEyeData = ImageEyeData;
                IsUploadedImage = true;
                * */
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        frameIn = new Mat();
        frameOut = new Mat();
        eyeCircleSelection = new Mat();
        JNIReturn = new Mat();
        JNIReturnNormalized = new Mat();
        framesPassed = 0;
    }

    @Override
    public void onCameraViewStopped() {
        frameIn.release();
        frameOut.release();
        eyeCircleSelection.release();
        //JNIReturn.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame)
    {
        //Crop the eye regions
        int radius = (int)Math.round(frameIn.width()*0.12);
        Point circleRCenter = new Point(frameIn.width()*0.25, frameIn.height()*0.3);
        Point circleLCenter = new Point(frameIn.width()*0.25, frameIn.height()*0.7);
        Rect eyeRegion = new Rect((int)Math.round(frameIn.width()*0.25-radius), (int)Math.round(frameIn.height()*0.3-radius), radius*2, radius*2);

        //Converts input frame to Mat
        frameIn = inputFrame.rgba();
        frameIn.copyTo(frameOut);
        eyeCircleSelection = frameIn.submat(eyeRegion);

        //Draw the onscreen circles
        Imgproc.circle(frameOut, circleRCenter, radius, new Scalar(255, 0, 0), 5);
        Imgproc.circle(frameOut, circleLCenter, radius, new Scalar(255, 0, 0), 5);

        if (IRISRECOGNITION == false)
            return frameOut;

        //Image capture
        if (framesPassed == 35)
        {
            framesPassed = 0;
            //jcv.flashOff();
            IRISRECOGNITION = false;
            //detectIris(eyeCircleSelection.getNativeObjAddr(),JNIReturn.getNativeObjAddr(), JNIReturnNormalized.getNativeObjAddr(), frameIn.getNativeObjAddr());

            //Intent getImageViewScreen = new Intent(this, ImageViewActivity.class);
            final int result = 1;
            Bitmap pass = Bitmap.createBitmap(JNIReturn.cols(), JNIReturn.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(JNIReturn, pass);
            //getImageViewScreen.putExtra( "sendingMat", pass );
            //startActivityForResult(getImageViewScreen, result);
        }
        framesPassed++;
        return frameOut;
    }



    //LBP feature extraction method
    public Vector<Integer> LBP(Mat input)
    {
        //Calculate pixel vectors
        Vector<Integer> outputHist = new Vector<Integer>();
        outputHist.setSize(59);
        Collections.fill(outputHist, 0);

        for (int i = 1; i < input.rows() - 1; i++)
        {
            for (int j = 1; j < input.cols() - 1; j++)
            {
                //Currently centered pixel
                double [] otherIntensity = input.get(i, j);

                int vectorValue = 0;
                Vector<Integer> binaryCode = new Vector<>();
                double pixelIntensity = otherIntensity[0];

                //Top left
                otherIntensity = input.get(i, j);
                if (otherIntensity[0] < pixelIntensity)
                {
                    vectorValue += 128;
                    binaryCode.add(1);
                }
                else
                    binaryCode.add(0);

                //Top middle
                otherIntensity = input.get(i, j - 1);
                if (otherIntensity[0] < pixelIntensity)
                {
                    vectorValue += 64;
                    binaryCode.add(1);
                }
                else
                    binaryCode.add(0);

                //Top right
                otherIntensity = input.get(i + 1, j - 1);
                if (otherIntensity[0] < pixelIntensity)
                {
                    vectorValue += 32;
                    binaryCode.add(1);
                }
                else
                    binaryCode.add(0);

                //Right
                otherIntensity = input.get(i + 1, j);
                if (otherIntensity[0] < pixelIntensity)
                {
                    vectorValue += 16;
                    binaryCode.add(1);
                }
                else
                    binaryCode.add(0);

                //Bottom right
                otherIntensity = input.get(i + 1, j + 1);
                if (otherIntensity[0] < pixelIntensity)
                {
                    vectorValue += 8;
                    binaryCode.add(1);
                }
                else
                    binaryCode.add(0);

                //Botttom middle
                otherIntensity = input.get(i, j + 1);
                if (otherIntensity[0] < pixelIntensity)
                {
                    vectorValue += 4;
                    binaryCode.add(1);
                }
                else
                    binaryCode.add(0);

                //Bottom left
                otherIntensity = input.get(i - 1, j + 1);
                if (otherIntensity[0] < pixelIntensity)
                {
                    vectorValue += 2;
                    binaryCode.add(1);
                }
                else
                    binaryCode.add(0);

                //Left
                otherIntensity = input.get(i - 1, j);
                if (otherIntensity[0] < pixelIntensity)
                {
                    vectorValue += 1;
                    binaryCode.add(1);
                }
                else
                    binaryCode.add(0);

                //Check if vector is uniform
                if (checkUniform(binaryCode))
                {
                    for (int x = 0; x < 59; x++)
                        if (histogramValues[x] == vectorValue)
                        {
                            int hold = outputHist.elementAt(x);
                            outputHist.remove(x);
                            outputHist.add(x, ++hold);
                            break;
                        }
                }
                else
                {
                    int hold = outputHist.elementAt(58);
                    outputHist.remove(58);
                    outputHist.add(58, ++hold);
                }
            }
        }
        return outputHist;
    }

    //Used in the LBP to check if a pixel vector is uniform
    public boolean checkUniform(Vector<Integer> binaryCode)
    {
        int transitionCount = 0;
        for (int i = 1; i < 8; i++)
        {
            if ((binaryCode.elementAt(i)^ binaryCode.elementAt(i-1)) == 1)
                transitionCount++;
            if (transitionCount > 2)
                return false;
        }
        return true;
    }

    //Used to compare LBP feature histograms
    public double chiSquared(Vector<Integer> hist1, Vector<Integer> hist2)
    {
        double[] normalizedHist1 = new double[59];
        double[] normalizedHist2 = new double[59];

        //Normalize the histogram
        for (int i = 0; i < 58; i++)
        {
            normalizedHist1[i] = (double) hist1.elementAt(i) / hist1.elementAt(58);
            normalizedHist2[i] = (double) hist2.elementAt(i) / hist2.elementAt(58);
        }

        normalizedHist1[58] = 1.0;
        normalizedHist2[58] = 1.0;

        //Compute the distance
        double chiSquaredValue = 0.0;
        for (int i = 1; i < 59; i++)
        {
            if (hist1.elementAt(i) + hist2.elementAt(i) != 0)
            {
                chiSquaredValue += Math.pow(normalizedHist1[i] - normalizedHist2[i], 2) / (normalizedHist1[i] + normalizedHist2[i]);
            }
        }
        return chiSquaredValue * 10;
    }

    //NBP feature extraction method
    public Vector<Integer> NBP(Mat input)
    {

        //Create NBP image
        Mat NBPimage = new Mat(input.rows(), input.cols(), CV_8U);
        Vector<Integer> NBPcode = new Vector<>();

        for (int i = 1; i < input.rows() - 1; i++)
        {
            for (int j = 1; j < input.cols() - 1; j++)
            {
                //Currently centered pixel
                double [] otherIntensity = input.get(i, j);

                int vectorValue = 0;
                Vector<Integer> binaryCode = new Vector<>();
                double pixelIntensity = otherIntensity[0];

                //Top left
                otherIntensity = input.get(i, j);
                if (otherIntensity[0] < pixelIntensity)
                    vectorValue += 128;

                //Top middle
                otherIntensity = input.get(i, j - 1);
                if (otherIntensity[0] < pixelIntensity)
                    vectorValue += 64;

                //Top right
                otherIntensity = input.get(i + 1, j - 1);
                if (otherIntensity[0] < pixelIntensity)
                    vectorValue += 32;


                //Right
                otherIntensity = input.get(i + 1, j);
                if (otherIntensity[0] < pixelIntensity)
                    vectorValue += 16;

                //Bottom right
                otherIntensity = input.get(i + 1, j + 1);
                if (otherIntensity[0] < pixelIntensity)
                    vectorValue += 8;

                //Botttom middle
                otherIntensity = input.get(i, j + 1);
                if (otherIntensity[0] < pixelIntensity)
                    vectorValue += 4;

                //Bottom left
                otherIntensity = input.get(i - 1, j + 1);
                if (otherIntensity[0] < pixelIntensity)
                    vectorValue += 2;

                //Left
                otherIntensity = input.get(i - 1, j);
                if (otherIntensity[0] < pixelIntensity)
                    vectorValue += 1;

                NBPimage.put(i, j, vectorValue);
            }
        }

        //Compute window means
        Vector<Vector<Integer>> means = new Vector<>();
        Vector<Integer> rowmeans;
        for (int i = 0; i < 6; i++)
        {
            rowmeans = new Vector<>();
            for (int j = 0; j < 12; j++)
            {
                int blockmean = 0;
                for (int x = i * 10; x < i * 10 + 10; x++)
                {
                    for (int y = j * 30; y < j * 30 + 30; y++)
                    {
                        blockmean += NBPimage.get(x, y)[0];
                    }
                }
                rowmeans.add(blockmean/(30*10));
            }
            means.add(rowmeans);
        }

        //Compute feature vector
        for (int i = 0; i < 6; i++)
        {
            for (int j = 0; j < 10; j++)
            {
                if (means.elementAt(i).elementAt(j) > means.elementAt(i).elementAt(j + 1))
                    NBPcode.add(1);
                else
                    NBPcode.add(0);
            }
        }
        return NBPcode;
    }

    //Used to compare NBP feature vectors
    public double hammingDistance(Vector<Integer> savedCode, Vector<Integer> inputCode)
    {
        int currentDistance = 0;
        int averageDistance = 0;
        for (int i = 0; i < inputCode.size(); i++)
        {
            currentDistance = 0;
            int  val = savedCode.elementAt(i) ^ inputCode.elementAt(i);
            while (val != 0)
            {
                currentDistance++;
                val &= val - 1;
            }
            averageDistance += currentDistance;
        }
        return 1.0*averageDistance / inputCode.size();
    }

    //Loads data into the userdb object from file
    public void loadUserDatabase()
    {
        try
        {
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}