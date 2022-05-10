package com.example.IrisREC.Function;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class FunctionImplement implements FunctionInterface{
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
}
