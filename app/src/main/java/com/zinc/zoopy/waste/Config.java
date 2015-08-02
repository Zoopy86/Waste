package com.zinc.zoopy.waste;

import android.app.Application;
import android.widget.EditText;

import com.activeandroid.ActiveAndroid;

/**
 * Created by Administrator on 28-07-15.
 */
public class Config extends Application {
    private String[] mCategories = {"Products", "Electronics", "Leisure Time", "Tickets"};
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
                }
                ActiveAndroid.setTransactionSuccessful();
            }
            finally {
                ActiveAndroid.endTransaction();
            }
        }
    }

    public static StringBuilder dateStringBuilder(int d, int m, int y) {
        return new StringBuilder().append(d).append("-").append(m + 1).append("-").append(y);
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
