package com.zinc.zoopy.waste;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCategory extends AppCompatActivity {
    EditText mEditText;
    Button mButton;
    Category mCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        mEditText = (EditText)findViewById(R.id.et_add_category);
        mButton = (Button)findViewById(R.id.b_save_new_category);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Config.isEmpty(mEditText)){
                    Toast.makeText(AddCategory.this, "Please, enter some letters", Toast.LENGTH_SHORT).show();
                }
                else {
                    mCategory = new Category();
                    mCategory.name = mEditText.getText().toString();
                    mCategory.save();
                    mEditText.getText().clear();
                    Toast.makeText(AddCategory.this, "Added new category: " + mCategory.name, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_category, menu);
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
