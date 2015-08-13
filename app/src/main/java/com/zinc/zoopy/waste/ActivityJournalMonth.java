package com.zinc.zoopy.waste;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ActivityJournalMonth extends AppCompatActivity {

    private ListView mListView;
    private AdapterJournalDay mAdapterJournalDay;
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
        mAdapterJournalDay = new AdapterJournalDay(this, Waste.groupByDay(Config.wasteDate));
        mListView.setAdapter(mAdapterJournalDay);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
                Waste waste = (Waste) mListView.getItemAtPosition(position);
                Intent intent = new Intent(ActivityJournalMonth.this, ActivityDayEntries.class);
                intent.putExtra("date", waste.dayAdded);
                startActivity(intent);
                overridePendingTransition(R.anim.move_left, R.anim.move_left2);
            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
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
