package com.example.IrisREC.Function;

import static android.content.ContentValues.TAG;

import android.util.Log;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import java.lang.annotation.Native;

public class NativeFunctionCall_IrisFunction {
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
    public native static void Segmentation(long addrInput, long addrOutput);

    public native static void Gaussian(long addrInput, long addrOutput);

    public native static void Canny(long addrInput,long addrOutput);

    public native static void FindIris(long addrInput,long addrOutput);

    public native static void PrintMat(long addrInput , long addrOutput);

    public native static void DetectIris( long addrOutput, long addrOutputNormalized, long addrOriginal);
}
