package com.zinc.zoopy.waste;

import android.provider.BaseColumns;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "Categories", id = BaseColumns._ID)
public class Category extends Model {
    @Column(name = "Name")
    public String name;


    public Category() {
        super();
    }

    public static String[] getAll() {
        List<Category> list = new Select()
                .from(Category.class)
                .execute();
        String[] categories = new String[list.size()];
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                categories[i] = list.get(i).name;
            }
        }

        return categories;
    }
}
