package com.zinc.zoopy.waste;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

@Table(name = "Categories", id = BaseColumns._ID)
public class Category extends Model {
    @Column(name = "Name")
    public String name;


    public Category() {
        super();
    }

    public static List<Category> getAll() {
        return new Select()
                .from(Category.class)
                .execute();
    }
    public static String[] getAllInArray(){
        String[] cats = new String[getAll().size()];
        for (int i = 0; i < cats.length; i++) {
            cats[i] = getAll().get(i).name;
        }
        return cats;
    }
    public static List<String> getAllNames(){
        List<String> names = new ArrayList<>();
        for(Category c: getAll()){
            names.add(c.name);
        }
        return names;
    }
}
