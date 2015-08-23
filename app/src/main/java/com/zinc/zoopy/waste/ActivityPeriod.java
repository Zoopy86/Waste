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
import android.widget.ListView;

import de.greenrobot.event.EventBus;

public class ActivityPeriod extends AppCompatActivity{
    ListView listView;
    ArrayAdapter<String> adapter;
    String[] periods;
    String extraPeriod;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period);

        initInstances();

    }

    private void initInstances() {
        listView = (ListView) findViewById(R.id.lv_periods);
        periods = getPeriods();
        extraPeriod = getIntent().getStringExtra("period");
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice,
                periods);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
        if (extraPeriod != null && !extraPeriod.equals("")) {
            for (int i = 0; i < periods.length; i++) {
                if (periods[i].equals(extraPeriod)) {
                    listView.setItemChecked(i, true);
                }
            }
        }
    }
    private String[] getPeriods(){
        String[] periods = new String[Periods.values().length];
        for (int i = 0; i < periods.length; i++) {
            periods[i] = Periods.values()[i].mLabel;
        }
        return periods;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.move_right2, R.anim.move_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_period, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_ok) {
            //EventBus.getDefault().post(new EventPeriod(periods[listView.getCheckedItemPosition()]));
            Intent intent = new Intent();
            Config.reportPeriod = periods[listView.getCheckedItemPosition()];
            intent.putExtra("period", periods[listView.getCheckedItemPosition()]);
            setResult(RESULT_OK, intent);
            finish();
        }
        if (id == android.R.id.home || id == R.id.back) {
            finish();
        }
        overridePendingTransition(R.anim.move_right2, R.anim.move_right);
        return super.onOptionsItemSelected(item);
    }
}
