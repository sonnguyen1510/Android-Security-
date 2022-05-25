package com.example.IrisREC.Function;

import static android.content.ContentValues.TAG;
import static org.opencv.core.CvType.CV_8U;
import static org.opencv.imgproc.Imgproc.GaussianBlur;
import static org.opencv.imgproc.Imgproc.HoughCircles;
import static org.opencv.imgproc.Imgproc.cvtColor;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.IrisREC.Function.Interface.IrisFunctionInterface;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.util.Vector;

public class IrisFunctionImplement implements IrisFunctionInterface {

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


    //-----------------------------FUNCTION-------------------------------
    public static Bitmap IrisLocalization(Bitmap input){
        //Convert bitmap input to ARGB type
        Bitmap ARGBBitmap= FunctionImplement.ARGBBitmap(input);
        //Convert to Mat
        Mat InputEye = new Mat(ARGBBitmap.getWidth(), ARGBBitmap.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(ARGBBitmap,InputEye);

        //Localization and Normalization
        Mat IrisLocal = new Mat();
        Mat IrisNormalize = new Mat();
        NativeFunctionCall_IrisFunction.DetectIris(IrisLocal.getNativeObjAddr(),IrisLocal.getNativeObjAddr(),IrisNormalize.getNativeObjAddr(),InputEye.getNativeObjAddr());

        //Convert to bitmap
        Bitmap Localization = Bitmap.createBitmap(IrisLocal .cols(), IrisLocal .rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(IrisLocal,Localization);

        return Localization;
    }

    public static Bitmap IrisNormalization(Bitmap input){
        //Convert bitmap input to ARGB type
        Bitmap ARGBBitmap= FunctionImplement.ARGBBitmap(input);
        //Convert to Mat
        Mat InputEye = new Mat(ARGBBitmap.getWidth(), ARGBBitmap.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(ARGBBitmap,InputEye);

        //Localization and Normalization
        Mat IrisLocal = new Mat();
        Mat IrisNormalize = new Mat();
        NativeFunctionCall_IrisFunction.DetectIris(IrisNormalize.getNativeObjAddr(),IrisLocal.getNativeObjAddr(),IrisNormalize.getNativeObjAddr(),InputEye.getNativeObjAddr());

        //Convert to bitmap
        Bitmap Normalization = Bitmap.createBitmap(IrisNormalize .cols(), IrisNormalize .rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(IrisNormalize,Normalization);

        return Normalization;
    }

    public static Mat IrisNormalization_ToMat(Bitmap input){
        //Convert bitmap input to ARGB type
        Bitmap ARGBBitmap= FunctionImplement.ARGBBitmap(input);
        //Convert to Mat
        Mat InputEye = new Mat(ARGBBitmap.getWidth(), ARGBBitmap.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(ARGBBitmap,InputEye);

        //Localization and Normalization
        Mat IrisLocal = new Mat();
        Mat IrisNormalize = new Mat();
        NativeFunctionCall_IrisFunction.DetectIris(IrisNormalize.getNativeObjAddr(),IrisLocal.getNativeObjAddr(),IrisNormalize.getNativeObjAddr(),InputEye.getNativeObjAddr());



        return IrisNormalize;
    }

    public static Bitmap IrisCanny(Bitmap input){
        //Convert bitmap input to ARGB type
        Bitmap ARGBBitmap= FunctionImplement.ARGBBitmap(input);
        //Convert to Mat
        Mat InputEye = new Mat(ARGBBitmap.getWidth(), ARGBBitmap.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(ARGBBitmap,InputEye);

        //Canny
        Mat IrisCanny = new Mat();
        NativeFunctionCall_IrisFunction.Canny(InputEye.getNativeObjAddr(),IrisCanny.getNativeObjAddr());


        //Conver mat to bit map
        Bitmap OutputBB = Bitmap.createBitmap(IrisCanny.cols(),IrisCanny.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(IrisCanny,OutputBB);

        return OutputBB;
    }

    public static Bitmap IrisGaussian(Bitmap input){
        //Convert bitmap input to ARGB type
        Bitmap ARGBBitmap= FunctionImplement.ARGBBitmap(input);
        //Convert to Mat
        Mat InputEye = new Mat(ARGBBitmap.getWidth(), ARGBBitmap.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(ARGBBitmap,InputEye);

        //Gaussian
        Mat IrisGaussian = new Mat();
        NativeFunctionCall_IrisFunction.Gaussian(InputEye.getNativeObjAddr(),IrisGaussian.getNativeObjAddr());


        //Convert mat to bit map
        Bitmap OutputBB = Bitmap.createBitmap(IrisGaussian.cols(),IrisGaussian.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(IrisGaussian,OutputBB);

        return OutputBB;
    }

    public static Bitmap findIris(Bitmap input){
        //Convert bitmap input to ARGB type
        Bitmap ARGBBitmap= FunctionImplement.ARGBBitmap(input);
        //Convert to Mat
        Mat InputEye = new Mat(ARGBBitmap.getWidth(), ARGBBitmap.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(ARGBBitmap,InputEye);

        //Gaussian
        Mat IrisGaussian = new Mat();
        NativeFunctionCall_IrisFunction.FindIris(InputEye.getNativeObjAddr(),IrisGaussian.getNativeObjAddr());


        //Convert mat to bit map
        Bitmap OutputBB = Bitmap.createBitmap(IrisGaussian.cols(),IrisGaussian.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(IrisGaussian,OutputBB);

        return OutputBB;
    }


    //
    public static Vector<Integer> Encode(Mat input)
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

    //Compare two iris
    public static double hammingDistance(Vector<Integer> savedCode, Vector<Integer> inputCode)
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


    /*
    public static Mat Hough_transform(Bitmap EyeInput){
        //Load OpenCV
        OpenCVLoader.initDebug();
        Boolean IsChecked = true;

        EyeInput = FunctionImplement.ARGBBitmap(EyeInput);

        Mat  InputData = new Mat(EyeInput.getWidth(), EyeInput.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(EyeInput, InputData);


    Mat gray = new Mat();
    cvtColor(InputData, gray, Imgproc.COLOR_RGB2GRAY);

        Imgproc.medianBlur(gray, gray, 15);

    Mat Canny = new Mat();
        Imgproc.Canny(InputData, Canny, 50, 200, 3, false);

    // convert source image to gray
    //org.opencv.imgproc.Imgproc.cvtColor(InputData, InputData, Imgproc.COLOR_BGR2GRAY);
//fliter

    //org.opencv.imgproc.Imgproc.blur(imgCny, imgCny, new Size(3, 3));
//apply canny

    //org.opencv.imgproc.Imgproc.Canny(imgCny, imgCny, 10, 30);
//apply Hough circle
    //------------------DRAW CIRCLE----------------------
    Mat circles = new Mat();
    Point pt;


    HoughCircles(gray, circles, CV_HOUGH_GRADIENT, 1.0,InputData.rows() / 10, 100.0, 30.0, 10, 60);
//draw the found circles
        for (int x = 0; x < circles.cols(); x++) {
        double[] c = circles.get(0, x);
        Point center = new Point(Math.round(c[0]), Math.round(c[1]));
        // circle center
        //Imgproc.circle(InputData, center, 1, new Scalar(0,100,100), 3, 8, 0 );
        // circle outline
        int radius = (int) Math.round(c[2]);
        Imgproc.circle(InputData, center, radius, new Scalar(0), CV_FILLED);
        int radius2 = (int) ((int)radius*2.4);
        Imgproc.circle(InputData, center, radius2, new Scalar(0), 2, 8, 0);

        IsChecked = false;
    }

        if(IsChecked){
        HoughCircles(gray, circles, CV_HOUGH_GRADIENT, 1.0,InputData.rows() / 10, 100.0, 30.0, 61, 200);
//draw the found circles
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            // circle center
            Imgproc.circle(InputData, center, 1, new Scalar(0,100,100), 3, 8, 0 );
            // circle outline
            int radius = (int ) Math.round(c[2]);
            Imgproc.circle(InputData, center, radius, new Scalar(0), CV_FILLED);
            int radius2 = (int) (radius/2.4);
            Imgproc.circle(InputData, center, radius2, new Scalar(0),2, 8, 0);
            IsChecked = false;
        }
    }



    //--------------------------------LINE---------------------------

        Mat linesP = new Mat (); // sẽ giữ kết quả của việc phát hiện
        Imgproc.HoughLinesP (Canny, linesP, 1, Math.PI / 180, 50, 50, 10); // chạy phát hiện thực tế

        for ( int x = 0; x <linesP.rows (); x ++) {
            double [] l = linesP.get (x, 0);
            Imgproc.line (InputData, new  Point (l [0], l [1]), new  Point (l [2], l [3]), new  Scalar (0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }

    //Bitmap pass = Bitmap.createBitmap(InputData.cols(), InputData.rows(), Bitmap.Config.ARGB_8888);
    //Utils.matToBitmap(InputData, pass);

        return InputData;
}

    public static Mat CropIrisImage(Mat InputIMG){
        Mat mask = new Mat(InputIMG.rows(), InputIMG.cols(), CV_8U, Scalar.all(0));
        Mat masked = new Mat();

        InputIMG.copyTo(mask);

        Mat thresh = new Mat();
        threshold( mask, thresh, 1, 255, Imgproc.THRESH_BINARY );

        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(thresh, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        Rect rect = Imgproc.boundingRect(contours.get(0));
        Mat cropped = InputIMG.submat(rect);

        return cropped;
    }

    * */



}
