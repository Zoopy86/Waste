package com.zinc.zoopy.waste;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.util.List;

import de.greenrobot.event.EventBus;


public class ActivitySortedEntries extends AppCompatActivity {
    ListView mListView;
    Spinner mSortSpinner;
    AdapterWastes mAdapterWastes;
    TextView mTotalSum;
    List<Waste> mWasteList;
    float wastesSum;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        EventBus.getDefault().register(this);
        mListView = (ListView)findViewById(R.id.statistic_listview);
        mSortSpinner = (Spinner)findViewById(R.id.spinner_sort);
        mTotalSum = (TextView)findViewById(R.id.total_sum);



        inflateSpinner();
        if(Config.wastesToShow == null) {
            mWasteList = Waste.getAll();
        }
        else mWasteList = Config.wastesToShow;
        Log.d("wListStatistic", "List SIZE" + mWasteList.size());

        //Log.d("WASTE", "" + mWasteList.get(0).dayAdded);
        Log.d("wListStatistic", "List SIZE" + mWasteList.size());
        mAdapterWastes = new AdapterWastes(this, mWasteList);
        mListView.setAdapter(mAdapterWastes);
        getWastesSum();

        mSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAdapterWastes = new AdapterWastes(ActivitySortedEntries.this, mWasteList);
                mListView.setAdapter(mAdapterWastes);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    void getWastesSum(){
        wastesSum = Waste.getSum(mWasteList);
        mTotalSum.setText("Wasted: " + wastesSum);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onEvent(EventDialog event){
        getWastesSum();
    }

    public void onEvent(EventRecord event){
        Log.d("RECEvent", "Event fired");
        mWasteList = event.records;
        mAdapterWastes = new AdapterWastes(this, mWasteList);
        mListView.setAdapter(mAdapterWastes);
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
        // Handle action bar item_waste clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            overridePendingTransition(R.anim.move_right2, R.anim.move_right);
        }
        if(id == R.id.action_new_waste){
            Intent intent = new Intent(this, ActivityMain.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
