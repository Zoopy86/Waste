package com.zinc.zoopy.waste;

import java.util.List;

/**
 * Created by Zoopy86 on 15-08-15.
 */
public class EventRecord {
    public List<Waste> records;
    public EventRecord(List<Waste> recordsList){
        this.records = recordsList;
    }
}
