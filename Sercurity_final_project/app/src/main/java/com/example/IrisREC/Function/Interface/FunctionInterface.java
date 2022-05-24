package com.example.IrisREC.Function.Interface;

import android.graphics.Bitmap;

import java.util.List;
import java.util.Vector;

public interface FunctionInterface {
    //converter
    static Bitmap ConvertStringBase64ToBitMap(String imageString) {
        return null;
    }

    static String ImageEyeStringBase64(Bitmap data) {
        return null;
    }

    static Bitmap bitmapFromRgba(int width, int height, byte[] bytes){
        return null;
    }

    static byte[] bitmapToRgba(Bitmap bitmap){
        return null;
    }

    static Bitmap ARGBBitmap(Bitmap img){
        return null;
    }

    public static List<Integer> ConvertVectorToList(Vector<Integer> vector){
        return null;
    }

    public static Vector<Integer> ConvertListToVector(List<Integer> vector){
        return null;
    }


}
