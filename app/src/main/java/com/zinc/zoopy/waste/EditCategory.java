package com.zinc.zoopy.waste;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

public class EditCategory extends AppCompatActivity {
    EditText mCategoryEditText;
    Button mSaveButton;
    long mCategoryID;
    Category mCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        mCategoryEditText = (EditText)findViewById(R.id.et_category);
        mSaveButton = (Button)findViewById(R.id.b_save_category);
        mCategoryID = getIntent().getLongExtra("category_id", 0);
        mCategory = Category.load(Category.class, mCategoryID);

        mCategoryEditText.setText(mCategory.name);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Config.isEmpty(mCategoryEditText)){
                    Toast.makeText(EditCategory.this, "Please, enter some letters", Toast.LENGTH_SHORT).show();
                }
                else {
                    mCategory.name = mCategoryEditText.getText().toString();
                    mCategory.save();
                    EventBus.getDefault().post(new EventBusDialogMessage());
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_category, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
