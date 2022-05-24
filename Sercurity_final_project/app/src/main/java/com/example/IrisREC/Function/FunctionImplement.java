package com.example.IrisREC.Function;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageView;

import com.example.IrisREC.Function.Interface.FunctionInterface;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Vector;

public class FunctionImplement implements FunctionInterface {

    public static Bitmap ConvertStringBase64ToBitMap(String imageString){
        byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedByte;
    }

    public static String ConvertBitmapToStringBase64(Bitmap data) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        data.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static class ConvertAndSetImageView extends AsyncTask<String, Bitmap, Bitmap> {


        public ImageView ImageEye;
        public Context context;


        public ConvertAndSetImageView(ImageView imageEye, Context context) {
            ImageEye = imageEye;
            this.context = context;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap img = ConvertStringBase64ToBitMap(strings[0]);
            this.publishProgress(img);
            return img;
        }

        @Override
        protected void onProgressUpdate(Bitmap... values) {
            super.onProgressUpdate(values);
            ImageEye.setImageBitmap(values[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
        }
    }

    public static byte[] bitmapToRgba(Bitmap bitmap) {
        if (bitmap.getConfig() != Bitmap.Config.ARGB_8888)
            throw new IllegalArgumentException("Bitmap must be in ARGB_8888 format");
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        byte[] bytes = new byte[pixels.length * 4];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int i = 0;
        for (int pixel : pixels) {
            // Get components assuming is ARGB
            int A = (pixel >> 24) & 0xff;
            int R = (pixel >> 16) & 0xff;
            int G = (pixel >> 8) & 0xff;
            int B = pixel & 0xff;
            bytes[i++] = (byte) R;
            bytes[i++] = (byte) G;
            bytes[i++] = (byte) B;
            bytes[i++] = (byte) A;
        }
        return bytes;
    }

    public static Bitmap bitmapFromRgba(int width, int height, byte[] bytes) {
        int[] pixels = new int[bytes.length / 4];
        int j = 0;

        for (int i = 0; i < pixels.length; i++) {
            int R = bytes[j++] & 0xff;
            int G = bytes[j++] & 0xff;
            int B = bytes[j++] & 0xff;
            int A = bytes[j++] & 0xff;

            int pixel = (A << 24) | (R << 16) | (G << 8) | B;
            pixels[i] = pixel;
        }


        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public static Bitmap ARGBBitmap(Bitmap img) {
        return img.copy(Bitmap.Config.ARGB_8888,true);
    }


    public static List<Integer> ConvertVectorToList(Vector<Integer> vector){
        List<Integer> Result = new Vector<>();

        for (int Element : vector){
            Result.add(Element);
        }

        return Result;
    }

    public static Vector<Integer> ConvertListToVector(List<Integer> vector){
        Vector<Integer> Result = new Vector<>();

        for (int Element : vector){
            Result.add(Element);
        }

        return Result;
    }



}
