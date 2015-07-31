package com.zinc.zoopy.waste;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import de.greenrobot.event.EventBus;


public class Statistic extends ActionBarActivity {
    ListView mListView;
    Spinner mSortSpinner;
    WastesAdapter mWastesAdapter;
    TextView mTotalSum;
    float wastesSum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        EventBus.getDefault().register(this);
        mListView = (ListView)findViewById(R.id.example_lv_list);
        mSortSpinner = (Spinner)findViewById(R.id.spinner_sort);
        mTotalSum = (TextView)findViewById(R.id.total_sum);

        inflateSpinner();

        mWastesAdapter = new WastesAdapter(this, Waste.getAll(SortTypes.UnixTime, true));
        mListView.setAdapter(mWastesAdapter);

        getWastesSum();

        mSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mWastesAdapter = new WastesAdapter(Statistic.this, Waste.getAll(SortTypes.values()[position], true));
                mListView.setAdapter(mWastesAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    void getWastesSum(){
        wastesSum = Waste.getSum();
        mTotalSum.setText("Wasted: " + wastesSum);
    }

    public void onEvent(EventBusMessage event){
        getWastesSum();
    }

    void inflateSpinner(){
        String[] sortTypes = new String[SortTypes.values().length];
        for(int i = 0; i< SortTypes.values().length; i++){
            sortTypes[i] = Config.sortTypeToString(SortTypes.values()[i]);
        }
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, sortTypes);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSortSpinner.setAdapter(adapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistic, menu);
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
        if(id == R.id.action_new_waste){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
