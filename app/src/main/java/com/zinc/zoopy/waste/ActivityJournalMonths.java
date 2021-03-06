package com.zinc.zoopy.waste;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ActivityJournalMonths extends AppCompatActivity {
    //TODO: Make ListView as swipe view
    private ListView mListView;
    private AdapterJournalDays mAdapterJournalDays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_month);
        String date = getIntent().getStringExtra("date");
        if(date != null && !date.equals("")){
            Config.wasteDate = date;
        }
        mListView = (ListView)findViewById(R.id.journal_list_days);
        setTitle(Config.monthFormat(Config.wasteDate));
        mAdapterJournalDays = new AdapterJournalDays(this, Waste.groupByDay(Config.wasteDate));
        mListView.setAdapter(mAdapterJournalDays);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
                Waste Waste = (Waste) mListView.getItemAtPosition(position);
                Intent intent = new Intent(ActivityJournalMonths.this, ActivityDayRecords.class);
                intent.putExtra("date", Waste.dayAdded);
                startActivity(intent);
                overridePendingTransition(R.anim.move_left, R.anim.move_left2);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_journal_month, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            overridePendingTransition(R.anim.move_right2, R.anim.move_right);
        }
        if(id == R.id.action_new_waste){
            Intent intent = new Intent(this, ActivityMain.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
