package com.zinc.zoopy.waste;
import android.provider.BaseColumns;
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

    public static float getSum(){
        float sum = 0f;
        List<Waste> wasteList = new Select()
                .from(Waste.class)
                .execute();
        for (int i = 0; i < wasteList.size() ; i++) {
            sum += wasteList.get(i).amount;
        }
        return sum;
    }

}

