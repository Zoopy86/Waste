package com.zinc.zoopy.waste;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 01-08-15.
 */
public class MyTimePickerDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    public static final int DIALOG_ID = 1;
    int pHour;
    int pMinute;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of DatePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        pHour = hour;
        pMinute = minute;
        EventBus.getDefault().post(new EventBusMessage(DIALOG_ID));
    }
}
