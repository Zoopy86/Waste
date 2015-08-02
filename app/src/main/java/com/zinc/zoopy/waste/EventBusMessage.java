package com.zinc.zoopy.waste;

/**
 * Created by Administrator on 30-07-15.
 */
public class EventBusMessage {
    public int dialogID = 0;
    public EventBusMessage(){

    }
    public EventBusMessage(int dialogID){
        this.dialogID = dialogID;
    }
}
