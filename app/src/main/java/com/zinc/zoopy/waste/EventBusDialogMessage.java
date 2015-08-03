package com.zinc.zoopy.waste;

/**
 * Created by Administrator on 30-07-15.
 */
public class EventBusDialogMessage {
    public int dialogID;
    public String category;
    public EventBusDialogMessage(){

    }
    public EventBusDialogMessage(int dialogID){
        this.dialogID = dialogID;
    }
    public EventBusDialogMessage(String category){
        this.category = category;
    }
}
