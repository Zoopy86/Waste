package com.zinc.zoopy.waste;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by Administrator on 28-07-15.
 */
public class Config extends Application {
    private String[] mCategories = {"Products", "Electronics", "Leisure Time", "Tickets"};
    private String[] mCurrencies = {"RUB", "USD", "EUR"};
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this, true);
        if(Category.getAll().length == 0){
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
        if(Currency.getAll().length == 0){
            ActiveAndroid.beginTransaction();
            try {
                for (int i = 0; i < mCurrencies.length; i++) {
                    Currency currencies = new Currency();
                    currencies.name = mCurrencies[i];
                    currencies.save();
                }
                ActiveAndroid.setTransactionSuccessful();
            }
            finally {
                ActiveAndroid.endTransaction();
            }
        }
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
