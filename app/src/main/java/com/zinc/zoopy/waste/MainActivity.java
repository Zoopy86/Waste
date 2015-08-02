package com.zinc.zoopy.waste;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import de.greenrobot.event.EventBus;


public class MainActivity extends AppCompatActivity {

    private EditText mNumberInput;
    private EditText mTextInput;
    private TextView mDateTextView;
    private TextView mTimeTextView;
    private Button mPickCategoryButton;
    private Button mSubmit;
    private String mCategory;
    private MyDatePickerDialog mMyDatePickerDialog = new MyDatePickerDialog();
    private MyTimePickerDialog mMyTimePickerDialog = new MyTimePickerDialog();
    private Waste waste = new Waste();
    final Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInstances();
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInputs();
                resetInputs();
            }
        });

    }

    void initInstances() {
        EventBus.getDefault().register(this);
        mNumberInput = (EditText) findViewById(R.id.ewa_et_amount);
        mTextInput = (EditText) findViewById(R.id.ewa_et_usernote);
        mDateTextView = (TextView) findViewById(R.id.tv_date);
        mTimeTextView = (TextView) findViewById(R.id.tv_time);
        mPickCategoryButton = (Button) findViewById(R.id.b_pick_category);
        mSubmit = (Button) findViewById(R.id.ewa_b_save);
        mDateTextView.setText(Config.dateStringBuilder(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH), c.get(Calendar.YEAR)));
        mTimeTextView.setText(Config.timeStringBuilder(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)));
        mCategory = getIntent().getStringExtra("category_name");
        if(mCategory == null || mCategory.matches("")){
            mCategory = "No category";
        }
        mPickCategoryButton.setText(mCategory);
        mPickCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PickCategory.class);
                startActivity(intent);
            }
        });
        mDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });
        mTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
            }
        });
    }

    private void updateTimeView() {
        this.mTimeTextView.setText(Config.timeStringBuilder(mMyTimePickerDialog.pHour, mMyTimePickerDialog.pMinute));
    }

    private void updateDateView() {
        this.mDateTextView.setText(Config.dateStringBuilder(mMyDatePickerDialog.pDay, mMyDatePickerDialog.pMonth, mMyDatePickerDialog.pYear));
    }

    public void setDate() {
        mMyDatePickerDialog.show(getSupportFragmentManager(), "datePicker");
    }

    public void setTime() {
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

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    //TODO refactor this code v1.0
    void saveInputs() {
        try {
            waste.amount = Float.parseFloat(mNumberInput.getText().toString());
            waste.category = mPickCategoryButton.getText().toString();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar waste_item clicks here. The action bar will
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
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}
