package com.zinc.zoopy.waste;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import com.activeandroid.ActiveAndroid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Zoopy86 on 28-07-15.
 */
public class Config extends Application {
    private String[] mDefaultCategories;
    public static String wasteDate; //For getting back from activity purposes
    public static String reportPeriod; //For getting back from activity purposes
    public static String[] selectedCategories;
    public static List<Waste> wastesToShow;
    public static Context context;
    public static int tabPosition;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        ActiveAndroid.initialize(this, true);
        mDefaultCategories = getResources().getStringArray(R.array.default_categories_array);
        if(Category.getAll().isEmpty()){
            ActiveAndroid.beginTransaction();
            try {
                for (String cat: mDefaultCategories) {
                    Category Category = new Category();
                    Category.name = cat;
                    Category.save();
                    Log.d("CAT", "Added Category: " + Category.name);
                }
                ActiveAndroid.setTransactionSuccessful();
            }
            finally {
                ActiveAndroid.endTransaction();
            }
        }

    }

    //3 jun 1889
    public static String dayFormat(String dayAdded){
        SimpleDateFormat simpleDateFormatIn = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormatOut = new SimpleDateFormat("d MMMM yyyy");
        Date date;
        try {
            date = simpleDateFormatIn.parse(dayAdded);
        } catch (ParseException e) {
            date = new Date();
            e.printStackTrace();
        }
        return simpleDateFormatOut.format(date);
    }
    //May 2015
    public static String monthFormat(String dayAdded){
        String d;
        String[] ruMonths = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};

        SimpleDateFormat simpleDateFormatIn = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormatOut = new SimpleDateFormat("MMMM yyyy");
        Date date;
        try {
            date = simpleDateFormatIn.parse(dayAdded);
        } catch (ParseException e) {
            date = new Date();
            e.printStackTrace();
        }
        d = simpleDateFormatOut.format(date);
        Log.d("DATE", d);
        for(String month: ruMonths){
            if(d.contains(month)){
                Log.d("DATE", month);
                d = d.replace(month, rusMonth(month));
                Log.d("DATE", d);
            }
        }
        return d;
    }
    public static StringBuilder dateStringBuilder(int d, int m, int y) {
        m = m + 1;
        String mD , mM, mY;
        if(d<10){mD = "0" + d;}else mD = "" + d;
        if(m<10){mM = "0" + m;}else mM = "" + m;
        if(y<10){mY = "0" + y;}else mY = "" + y;
        return new StringBuilder().append(mD).append("-").append(mM).append("-").append(mY);
    }
    static String rusMonth(String month){
        String m;
        switch (month){
            case "января":  m = "Январь";  break;
            case "февраля": m = "Февраль"; break;
            case "марта":   m = "Март";    break;
            case "апреля":  m = "Апрель";  break;
            case "мая":     m = "Май";     break;
            case "июня":    m = "Июнь";    break;
            case "июля":    m = "Июль";    break;
            case "августа": m = "Август";  break;
            case "сентября":m = "Сентябрь";break;
            case "октября": m = "Октябрь"; break;
            case "ноября":  m = "Ноябрь";  break;
            case "декабря": m = "Декабрь"; break;
            default: m = month; break;
        }
        return m;
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
