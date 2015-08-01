package com.zinc.zoopy.waste;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import de.greenrobot.event.EventBus;


public class MainActivity extends AppCompatActivity {

    private EditText mNumberInput;
    private EditText mTextInput;
    private TextView mDateTextView;
    private TextView mTimeTextView;
    private Spinner mSpinnerCurrency;
    private Spinner mSpinnerCategory;
    private Button mSubmit;

    private MyDatePickerDialog mMyDatePickerDialog = new MyDatePickerDialog();
    private MyTimePickerDialog mMyTimePickerDialog = new MyTimePickerDialog();
    private Waste waste = new Waste();
    final Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInstances();
        inflateSpinners();
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInputs();
                resetInputs();
            }
        });
    }

    private void updateTimeView() {
        this.mTimeTextView.setText(timeStringBuilder(mMyTimePickerDialog.pHour, mMyTimePickerDialog.pMinute));
    }

    private void updateDateView() {
        this.mDateTextView.setText(dateStringBuilder(mMyDatePickerDialog.pDay, mMyDatePickerDialog.pMonth, mMyDatePickerDialog.pYear));
    }

    private StringBuilder dateStringBuilder(int d, int m, int y) {
        return new StringBuilder().append(d).append("-").append(m + 1).append("-").append(y);
    }

    private StringBuilder timeStringBuilder(int h, int m) {
        if (m < 10)
            return new StringBuilder().append(h).append(":").append("0" + m);
        else
            return new StringBuilder().append(h).append(":").append(m);
    }

    public void setDate(View v) {
        mMyDatePickerDialog.show(getSupportFragmentManager(), "datePicker");
    }

    public void setTime(View v) {
        mMyTimePickerDialog.show(getSupportFragmentManager(), "timePicker");
    }

    public void onEvent(EventBusMessage event) {
        switch (event.dialogID) {
            case MyDatePickerDialog.DIALOG_ID:
                updateDateView();
                break;
            case MyTimePickerDialog.DIALOG_ID:
                updateTimeView();
                break;
        }

    }

    void initInstances() {
        EventBus.getDefault().register(this);
        mNumberInput = (EditText) findViewById(R.id.ewa_et_amount);
        mTextInput = (EditText) findViewById(R.id.ewa_et_usernote);
        mDateTextView = (TextView) findViewById(R.id.tv_date);
        mTimeTextView = (TextView) findViewById(R.id.tv_time);
        mSubmit = (Button) findViewById(R.id.ewa_b_save);
        mSpinnerCurrency = (Spinner) findViewById(R.id.spinner_currency);
        mSpinnerCategory = (Spinner) findViewById(R.id.spinner_category);
        mDateTextView.setText(dateStringBuilder(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH), c.get(Calendar.YEAR)));
        mTimeTextView.setText(timeStringBuilder(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)));
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    //TODO refactor this code v1.0
    void saveInputs() {
        try {
            waste.amount = Float.parseFloat(mNumberInput.getText().toString());
            waste.category = mSpinnerCategory.getSelectedItem().toString();
            waste.currency = mSpinnerCurrency.getSelectedItem().toString();
            waste.unixTime = System.currentTimeMillis();
            waste.dayAdded = mDateTextView.getText().toString();
            waste.timeAdded = mTimeTextView.getText().toString();
            waste.userNote = mTextInput.getText().toString();
            waste.save();
            showToast("Amount: " + waste.amount + ". \nCategory: " + waste.category + ". \nComment: " + waste.userNote + ". \nDate: " + waste.timeAdded);
        } catch (Exception e) {
            Log.d("Invalid Input", "Exception: " + e.toString());
            showToast("Please, input number different from 0");
        }
    }


    void resetInputs() {
        mNumberInput.setText("");
        mTextInput.setText("");
        mNumberInput.requestFocus();
    }

    void inflateSpinners() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterCurrency = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Currency.getAll());
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Category.getAll());
        // Specify the layout to use when the list of choices appears
        adapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinnerCurrency.setAdapter(adapterCurrency);
        mSpinnerCategory.setAdapter(adapterCategory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.statistic) {
            Intent intent = new Intent(getApplicationContext(), Statistic.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    void showToast(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
    }
}
