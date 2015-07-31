package com.zinc.zoopy.waste;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class EditWasteActivity extends ActionBarActivity {
    public static long mID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_waste);
        Intent intent = getIntent();
        mID = intent.getLongExtra("id", 0);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_waste, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        Spinner mSpinnerCurrency;
        Spinner mSpinnerCategory;
        EditText amount;
        EditText userNote;
        Button mSaveButton;
        Context mContext;
        Waste waste = Waste.load(Waste.class, mID);
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_edit_waste, container, false);
            amount = (EditText)rootView.findViewById(R.id.ewa_et_amount);
            userNote = (EditText)rootView.findViewById(R.id.ewa_et_usernote);
            mSaveButton = (Button)rootView.findViewById(R.id.ewa_b_save);
            mContext = rootView.getContext();
            mSpinnerCurrency = (Spinner) rootView.findViewById(R.id.spinner_currency);
            mSpinnerCategory = (Spinner)rootView.findViewById(R.id.spinner_category);
            inflateSpinners();

            //Log.d("INTENT RECIEVE", "id = " + mID);
            mSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    try {
                        waste.amount = Float.parseFloat(amount.getText().toString());
                    } catch (Exception e) {
                        Log.e("logtag", "Exception: " + e.toString());
                    }
                    waste.userNote = userNote.getText().toString();
                    waste.category = mSpinnerCategory.getSelectedItem().toString();
                    waste.currency = mSpinnerCurrency.getSelectedItem().toString();
                    waste.save();
                    Intent intent = new Intent(mContext, Statistic.class);
                    startActivity(intent);

                }
            });
            amount.setText(waste.amount.toString());
            userNote.setText(waste.userNote);

            return rootView;
        }
        void inflateSpinners(){
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<String> adapterCurrency = new ArrayAdapter<>(mContext,android.R.layout.simple_spinner_item, Currency.getAll());
            ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(mContext,android.R.layout.simple_spinner_item, Category.getAll());
            // Specify the layout to use when the list of choices appears
            adapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            mSpinnerCurrency.setAdapter(adapterCurrency);
            mSpinnerCurrency.setSelection(adapterCurrency.getPosition(waste.currency));
            mSpinnerCategory.setAdapter(adapterCategory);
            mSpinnerCategory.setSelection(adapterCategory.getPosition(waste.category));
        }
    }
}
