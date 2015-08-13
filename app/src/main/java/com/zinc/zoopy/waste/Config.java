package com.zinc.zoopy.waste;

import android.app.Application;
import android.util.Log;
import android.widget.EditText;

import com.activeandroid.ActiveAndroid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 28-07-15.
 */
public class Config extends Application {
    private String[] mCategories = {"Products", "Electronics", "Leisure Time", "Tickets"};
    public static String wasteDate;
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this, true);
        if(Category.getAll().isEmpty()){
            ActiveAndroid.beginTransaction();
            try {
                for (int i = 0; i < mCategories.length; i++) {
                    Category category = new Category();
                    category.name = mCategories[i];
                    category.save();
                    Log.d("CAT", "Saved category: " + category.name);
                }
                ActiveAndroid.setTransactionSuccessful();
            }
            finally {
                ActiveAndroid.endTransaction();
            }
        }
    }
    public static String dayFormat(String dayAdded){
        SimpleDateFormat simpleDateFormatIn = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormatOut = new SimpleDateFormat("dd MMMM yyyy");
        Date date;
        try {
            date = simpleDateFormatIn.parse(dayAdded);
        } catch (ParseException e) {
            date = new Date();
            e.printStackTrace();
        }
        return simpleDateFormatOut.format(date);
    }

    public static String monthFormat(String dayAdded){
        SimpleDateFormat simpleDateFormatIn = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormatOut = new SimpleDateFormat("MMMM yyyy");
        Date date;
        try {
            date = simpleDateFormatIn.parse(dayAdded);
        } catch (ParseException e) {
            date = new Date();
            e.printStackTrace();
        }
        return simpleDateFormatOut.format(date);
    }

    public static StringBuilder dateStringBuilder(int d, int m, int y) {
        m = m + 1;
        String mD , mM, mY;
        if(d<10){mD = "0" + d;}else mD = "" + d;
        if(m<10){mM = "0" + m;}else mM = "" + m;
        if(y<10){mY = "0" + y;}else mY = "" + y;
        return new StringBuilder().append(mD).append("-").append(mM).append("-").append(mY);
    }

    public static StringBuilder timeStringBuilder(int h, int m) {
        if (m < 10)
            return new StringBuilder().append(h).append(":").append("0" + m);
        else
            return new StringBuilder().append(h).append(":").append(m);
    }

    public static boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    //TODO: should not use this code
    public static String sortTypeToString(SortTypes sortTypes){
        switch (sortTypes){
            case Amount: return "Amount";
            case TimeAdded: return "Time Added";
            case UnixTime: return "Unix Time";
            case Currency: return "Currency";
            case UserNote: return "User Note";
            default: return "";
        }
    }

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
}
