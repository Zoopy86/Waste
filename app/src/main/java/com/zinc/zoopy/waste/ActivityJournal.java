package com.zinc.zoopy.waste;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ActivityJournal extends AppCompatActivity {
    ListView mListView;
    AdapterJournalMonths mAdapterJournalMonths;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        mListView = (ListView)findViewById(R.id.journal_list_dates);
        mAdapterJournalMonths = new AdapterJournalMonths(this, Waste.groupByYearAndMonth());
        mListView.setAdapter(mAdapterJournalMonths);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
                Waste Waste = (Waste)mListView.getItemAtPosition(position);
                Intent intent = new Intent(ActivityJournal.this, ActivityJournalMonths.class);
                intent.putExtra("date", Waste.dayAdded);
                startActivity(intent);
                overridePendingTransition(R.anim.move_left, R.anim.move_left2);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_journal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_new_waste){
            Intent intent = new Intent(this, ActivityMain.class);
            startActivity(intent);
            overridePendingTransition(R.anim.move_right2, R.anim.move_right);
            finish();
        }
        if (id == R.id.report) {
            Intent intent = new Intent(getApplicationContext(), ActivityReport.class);
            startActivity(intent);
            overridePendingTransition(R.anim.move_left, R.anim.move_left2);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
