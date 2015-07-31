package com.zinc.zoopy.waste;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
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


public class MainActivity extends AppCompatActivity {

    EditText mNumberInput;
    EditText mTextInput;
    TextView mDate;
    TextView mTime;
    Spinner mSpinnerCurrency;
    Spinner mSpinnerCategory;
    long mUnixTime;
    Button mSubmit;
    Time mNow = new Time();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInstances();
        inflateSpinners();
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new Delete().from(Waste.class).execute();
                saveInput();
                resetInputs();
            }
        });
    }

    void initInstances(){
        mNow.setToNow();
        mNumberInput = (EditText) findViewById(R.id.ewa_et_amount);
        mTextInput = (EditText) findViewById(R.id.ewa_et_usernote);
        mDate = (TextView)findViewById(R.id.tv_date);
        mTime = (TextView)findViewById(R.id.tv_time);
        mSubmit = (Button) findViewById(R.id.ewa_b_save);
        mSpinnerCurrency = (Spinner) findViewById(R.id.spinner_currency);
        mSpinnerCategory = (Spinner) findViewById(R.id.spinner_category);
    }
    //TODO refactor this code v1.0
    void saveInput() {
        Waste waste = new Waste();
        float number;
        String text = mTextInput.getText().toString();


        String day = mNow.format("%YYYY-%MM-%DD");
        String time = mNow.format("%H:%M");
        mUnixTime = System.currentTimeMillis();

        try {
            number = Float.parseFloat(mNumberInput.getText().toString());
            waste.category = mSpinnerCategory.getSelectedItem().toString();
            waste.amount = number;
            waste.currency = mSpinnerCurrency.getSelectedItem().toString();
            waste.unixTime = mUnixTime;
            waste.dayAdded = day;
            waste.timeAdded = time;
            waste.userNote = text;
            waste.save();
            showToast("You spent: " + waste.amount + ". \nWhere: " + waste.userNote + ". \nWhen: " + waste.timeAdded);
        } catch (Exception e) {
            Log.d("Invalid Input", "Exception: " + e.toString());
            showToast("Please, input number different from 0");
        }
    }

    void showToast(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
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
}
