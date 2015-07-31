package com.zinc.zoopy.waste;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Administrator on 30-07-15.
 */
@Table(name = "Currencies", id = BaseColumns._ID)
public class Currency extends Model {

    @Column(name = "Name")
    public String name;

    public Currency(){
        super();
    }

    public static String[] getAll() {
        List<Currency> list = new Select()
                .from(Currency.class)
                .execute();
        String[] currencies = new String[list.size()];
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                currencies[i] = list.get(i).name;
            }
        }

        return currencies;
    }

}
