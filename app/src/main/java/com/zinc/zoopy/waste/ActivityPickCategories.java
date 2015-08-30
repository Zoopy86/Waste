package com.zinc.zoopy.waste;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityPickCategories extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> adapter;
    String[] categories;
    String[] extraCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_categories);
        initInstances();
    }

    private void initInstances() {
        listView = (ListView) findViewById(R.id.lv_categories_multiple);
        categories = Category.getAllInArray();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice,
                categories);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        extraCategories = getIntent().getStringArrayExtra("selectedCategories");
        if (extraCategories != null) {
            for (String s : extraCategories) {
                for (int i = 0; i < categories.length; i++) {
                    if (categories[i].equals(s)) {
                        listView.setItemChecked(i, true);
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_pick_categories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_ok) {
            SparseBooleanArray checked = listView.getCheckedItemPositions();
            ArrayList<String> selectedItems = new ArrayList<>();
            for (int i = 0; i < checked.size(); i++) {

                int position = checked.keyAt(i);

                if (checked.valueAt(i))
                    selectedItems.add(adapter.getItem(position));
            }
            if (selectedItems.size() == 0) {
                Toast.makeText(this, R.string.error_no_category_selected, Toast.LENGTH_SHORT).show();
            } else {
                String[] outputStrArr = new String[selectedItems.size()];

                for (int i = 0; i < selectedItems.size(); i++) {
                    outputStrArr[i] = selectedItems.get(i);
                }
                Config.selectedCategories = outputStrArr;
                Intent intent = new Intent();
                intent.putExtra("selectedCategories", outputStrArr);
                setResult(RESULT_OK, intent);
                // start the ResultActivity
                finish();
                overridePendingTransition(R.anim.move_right2, R.anim.move_right);
            }
        }
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            overridePendingTransition(R.anim.move_right2, R.anim.move_right);
        }
        return super.onOptionsItemSelected(item);
    }
}
