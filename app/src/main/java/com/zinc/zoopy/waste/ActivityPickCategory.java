package com.zinc.zoopy.waste;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class ActivityPickCategory extends AppCompatActivity {
    ListView mListView;
    Button mAddCategoryButton;
    AdapterCategory mAdapterCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_category);

        mListView = (ListView)findViewById(R.id.category_listview);
        mAddCategoryButton = (Button)findViewById(R.id.b_add_new_category);

        mAdapterCategory = new AdapterCategory(this, Category.getAll());
        mListView.setAdapter(mAdapterCategory);
        mAddCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityPickCategory.this, ActivityAddCategory.class);
                startActivity(intent);
                overridePendingTransition(R.anim.move_left, R.anim.move_left2);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pick_category, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item_waste clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
