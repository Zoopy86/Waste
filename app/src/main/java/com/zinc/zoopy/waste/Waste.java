package com.zinc.zoopy.waste;
import android.content.Context;
import android.content.res.Resources;
import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Zoopy86 on 28-07-15.
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
     * ALL DATES MUST BE IN YYYY-MM-DD FORMAT
     */
    public static List<Waste> getBetweenDate(String startDate, String endDate){
        return new Select()
                .from(Waste.class)
                .where("DayAdded BETWEEN strftime('%Y-%m-%d', ?) AND strftime('%Y-%m-%d', ?)", startDate, endDate)
                .orderBy("DayAdded DESC")
                .execute();
    }
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
    public static List<Waste> getWhere(String... categories){
        String category = "";
        for(String closure: categories){
            if(closure.equals(categories[categories.length - 1])) {
                category += "'" + closure + "'";
            }
            else category += "'" + closure + "'" + " OR Category LIKE ";
        }
        return new Select()
                .from(Waste.class)
                .where("Category LIKE " + category)
                .orderBy("DayAdded DESC")
                .execute();
    }
//    public static List<Waste> getWhere(String startDate, String endDate, String... categories){
//        String category = "";
//        for(String closure: categories){
//            if(closure.equals(categories[categories.length - 1])) {
//                category += "'" + closure + "'";
//            }
//            else category += "'" + closure + "'" + " OR Category LIKE ";
//        }
//        return new Select()
//                .from(Waste.class)
//                .where("DayAdded BETWEEN ? AND ? AND Category LIKE " + category, startDate, endDate)
//                .orderBy("DayAdded DESC")
//                .execute();
//    }
    public static float getSum(List<Waste> wasteList){
        float sum = 0f;
        for (int i = 0; i < wasteList.size() ; i++) {
            sum += wasteList.get(i).amount;
        }
        return sum;
    }

    public static List<Waste> getRecordsByPeriod(String period) {
        Calendar c = Calendar.getInstance();
        String today = Config.dateStringBuilder(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).toString();

        if (period.equals(Config.context.getString(R.string.period_all))) {
            return Waste.getAll();
        } else if (period.equals(Config.context.getString(R.string.period_today))) {
            return Waste.getBetweenDate(today, today);
        } else if (period.equals(Config.context.getString(R.string.period_this_week))) {
            c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
            String week = Config.dateStringBuilder(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).toString();
            return Waste.getBetweenDate(week, today);
        } else if (period.equals(Config.context.getString(R.string.period_this_month))) {
            c.set(Calendar.DAY_OF_MONTH, 1);
            String thisMonth = Config.dateStringBuilder(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).toString();
            return Waste.getBetweenDate(thisMonth, today);
        } else if (period.equals(Config.context.getString(R.string.period_last_month))) {
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.add(Calendar.MONTH, -1);
            String lastMonthFirstDay = Config.dateStringBuilder(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).toString();
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            String lastMonthLastDay = Config.dateStringBuilder(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).toString();
            return Waste.getBetweenDate(lastMonthFirstDay, lastMonthLastDay);
        } else if (period.equals(Config.context.getString(R.string.period_this_year))) {
            c.set(Calendar.DAY_OF_YEAR, 1);
            String thisYear = Config.dateStringBuilder(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).toString();
            return Waste.getBetweenDate(thisYear, today);
        } else if (period.equals(Config.context.getString(R.string.period_last_year))) {
            c.set(Calendar.DAY_OF_YEAR, 1);
            c.add(Calendar.YEAR, -1);
            String lastYearFirstDay = Config.dateStringBuilder(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).toString();
            c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR));
            String lastYearLastDay = Config.dateStringBuilder(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).toString();
            return Waste.getBetweenDate(lastYearFirstDay, lastYearLastDay);
        } else return Waste.getAll();
    }

    public static List<Waste> getRecordsByCategory(String... cats) {
        return Waste.getWhere(cats);
    }

    public static List<Waste> getRetainedRecords(List<Waste> list1, List<Waste> list2) {
        if (list1.size() < list2.size()) {
            list2.retainAll(list1);
            return list2;
        } else {
            list1.retainAll(list2);
            return list1;
        }

    }
    //TODO: make delete by month and day functions

}

