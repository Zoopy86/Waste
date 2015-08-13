package com.zinc.zoopy.waste;
import android.provider.BaseColumns;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Administrator on 28-07-15.
 */
@Table(name = "Waste", id = BaseColumns._ID)
public class Waste extends Model {
    @Column(name = "Amount")
    public Float amount;
    @Column(name = "UserNote")
    public String userNote;
    @Column(name = "DayAdded")
    public String dayAdded;
    @Column(name = "TimeAdded")
    public String timeAdded;
    @Column(name = "UnixTime")
    public long unixTime;
    @Column(name = "Category")
    public String category;


    public Waste(){super();}

    public static List<Waste> getAll(SortTypes sort, boolean asc) {
        if(asc) return new Select()
                .from(Waste.class)
                .orderBy(sort.toString() + " ASC")
                .execute();

        else return new Select()
                .from(Waste.class)
                .orderBy(sort.toString() + " DESC")
                .execute();
    }
    public static List<Waste> getAll(){
        return new Select()
                .from(Waste.class)
                .execute();
    }

    /*
     * @param startDate Must be in YYYY-MM-DD format
     * @param endDate Must be in YYYY-MM-DD format
     */
    public static List<Waste> getBetweenDate(String startDate, String endDate){
        return new Select()
                .from(Waste.class)
                .where("DayAdded BETWEEN ? AND ?", startDate, endDate)
                .orderBy("DayAdded DESC")
                .execute();
    }
    //@param month must be in MM format
    public static List<Waste> groupByYearAndMonth(){
        return new Select()
                .from(Waste.class)
                .groupBy("strftime('%Y-%m', DayAdded)")
                .orderBy("DayAdded DESC")
                .execute();
    }
    public static List<Waste> getByYearAndMonth(String dayAdded){
        dayAdded = dayAdded.substring(0, 7);
        return new Select()
                .from(Waste.class)
                .where("strftime('%Y-%m', DayAdded) LIKE ?", dayAdded)
                .orderBy("DayAdded DESC")
                .execute();
    }
    public static List<Waste> groupByDay(String month){
        month = month.substring(0, 7);
        return new Select()
                .from(Waste.class)
                .where("strftime('%Y-%m', DayAdded) LIKE ?", month)
                .groupBy("strftime('%Y-%m-%d', DayAdded)")
                .orderBy("DayAdded DESC")
                .execute();
    }
    public static List<Waste> getByDay(String dayAdded){
        return new Select()
                .from(Waste.class)
                .where("strftime('%Y-%m-%d', DayAdded) LIKE ?", dayAdded)
                .orderBy("DayAdded DESC")
                .execute();
    }
    public static float getSum(List<Waste> wasteList){
        float sum = 0f;
        for (int i = 0; i < wasteList.size() ; i++) {
            sum += wasteList.get(i).amount;
        }
        return sum;
    }


}

