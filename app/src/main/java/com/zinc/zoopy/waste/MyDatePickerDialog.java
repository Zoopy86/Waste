package com.zinc.zoopy.waste;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 01-08-15.
 */
public class MyDatePickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    public static final int DIALOG_ID = 0;
    int pYear;
    int pDay;
    int pMonth;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        pYear = year;
        pDay = day;
        pMonth = month;
        EventBus.getDefault().post(new EventBusMessage(DIALOG_ID));
    }
}
