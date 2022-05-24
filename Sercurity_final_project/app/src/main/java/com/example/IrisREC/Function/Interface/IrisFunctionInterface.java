package com.example.IrisREC.Function.Interface;

import android.graphics.Bitmap;

import org.opencv.core.Mat;

import java.util.List;
import java.util.Vector;

public interface IrisFunctionInterface {

    public static Bitmap IrisLocalization(Bitmap input) {
        return null;
    }

    public static Bitmap IrisNormalization(Bitmap input){
        return null;
    }

    public static Vector<Integer> Encode(Mat input){
        return null;
    }

    public static double hammingDistance(Vector<Integer> savedCode, Vector<Integer> inputCode){
        return 0;
    }

    public static Bitmap IrisCanny(Bitmap input){
        return null;
    }

    public static Bitmap IrisGaussian(Bitmap input){
        return null;
    }


}
