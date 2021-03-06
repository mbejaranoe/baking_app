package com.example.android.mbejaranoe.bakingapp.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Manolo on 03/10/2017.
 * Utility methods for easily make operations in the Internet
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    /* Download an image from the internet having its url in String format */
    public static Bitmap getImageFromURL(String url){
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Bitmap bitmap2 = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            return bitmap2;
        } catch (Exception e) {
            Log.e("Error", "" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
