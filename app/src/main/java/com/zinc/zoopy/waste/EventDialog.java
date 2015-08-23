package com.zinc.zoopy.waste;

/**
 * Created by Zoopy86 on 30-07-15.
 */
public class EventDialog {
    public int dialogID;
    public String category;
    public EventDialog(){

    }
    public EventDialog(int dialogID){
        this.dialogID = dialogID;
    }
    public EventDialog(String category){
        this.category = category;
    }
}
