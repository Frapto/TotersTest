package com.frapto.toterstest.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class Utilities {
    /**
     * assume that the logged in user has the ID of 1,
     * in reality this will be saved in somewhere internally after the user authenticates with the backend.
     * This is used to identify which messages are the ones that the user sent and which are received
     * */
    public static long MY_ID = 1;
    private static Random random = new Random();
    /**
     * color of the message when I am the sender
     * */
    public static final String SENDER_COLOR = "#E5DCEF";
    /**
     * color of the message when I am not the sender (aka I am the receiver)
     * */
    public static final String RECEIVER_COLOR = "#E6EFDC";
    /**
     * hides the keyboard
     * */
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * called by convertToMessageTime to set the time to hh:mm
     * */
    public static String formatToTime(Calendar calendar) {

        String date = DateFormat.format("hh:mm", calendar).toString();
        return date;
    }

    /**
     * called by convertToMessageTime to set the time to dd-MM hh:mm
     * */
    public static String formatToDateTime(Calendar calendar) {
        String date = DateFormat.format("dd-MM hh:mm", calendar).toString();
        return date;
    }
    /**
     * changes a long value to either:
     *      hh:mm (if it is today)
     *      or
     *      dd-MM hh:mm (if not today)
     * */
    public static String convertToMessageTime(long time){
        if(time==0)
        {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        if(isToday(calendar))
            return formatToTime(calendar);
        else
            return formatToDateTime(calendar);
    }
    //checks a calendar if it is today
    public static boolean isToday(Calendar toBeTested){
        Calendar current = Calendar.getInstance();
        return  toBeTested.get(Calendar.YEAR) == current.get(Calendar.YEAR)
                && toBeTested.get(Calendar.MONTH) == current.get(Calendar.MONTH)
                && toBeTested.get(Calendar.DAY_OF_MONTH) == current.get(Calendar.DAY_OF_MONTH);
    }
    public static Bitmap generateRandomBitmap(int width, int height){

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                //each can go from 0 to 255 (256 is not included)
                int r = random.nextInt(256);
                int g = random.nextInt(256);
                int b= random.nextInt(256);
                int a = random.nextInt(256);
                bmp.setPixel(x, y, Color.argb(a, r, g, b));
            }
        }
        return bmp;
    }
    /**
     * saves a bitmap in the internal storage at the given path
     * */
    public static void saveBitmap(Bitmap bmp, String path){
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }

        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.close();
            bmp.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
