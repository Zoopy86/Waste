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

    public static List<Category> getAll() {
        List<Category> list = new Select()
                .from(Category.class)
                .execute();
        return list;
    }
}
