package com.zinc.zoopy.waste;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

import de.greenrobot.event.EventBus;

/**
 * Created by Zoopy86 on 01-08-15.
 */
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    public static final int DIALOG_ID = 0;
    int pYear;
    int pDay;
    int pMonth;

    @Override
    public void onStart() {
        super.onStart();
    }

    @NonNull
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        pYear = year;
        pDay = day;
        pMonth = month;
        EventBus.getDefault().post(new EventDialog(DIALOG_ID));
    }
}
