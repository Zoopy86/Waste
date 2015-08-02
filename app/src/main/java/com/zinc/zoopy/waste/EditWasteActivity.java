package com.zinc.zoopy.waste;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import de.greenrobot.event.EventBus;


public class EditWasteActivity extends AppCompatActivity {

    public static long mID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_waste);
        mID = getIntent().getLongExtra("id", 0);
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
        // Handle action bar waste_item clicks here. The action bar will
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

        EditText amount;
        EditText userNote;
        Button mSaveButton;
        Context mContext;
        TextView mDateTextView;
        TextView mTimeTextView;
        Waste waste = Waste.load(Waste.class, mID);

        MyDatePickerDialog mMyDatePickerDialog = new MyDatePickerDialog();
        MyTimePickerDialog mMyTimePickerDialog = new MyTimePickerDialog();

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            EventBus.getDefault().register(this);
            final View rootView = inflater.inflate(R.layout.fragment_edit_waste, container, false);
            amount = (EditText) rootView.findViewById(R.id.ewa_et_amount);
            userNote = (EditText) rootView.findViewById(R.id.ewa_et_usernote);
            mSaveButton = (Button) rootView.findViewById(R.id.ewa_b_save);
            mContext = rootView.getContext();
            mDateTextView = (TextView) rootView.findViewById(R.id.tv_date);
            mTimeTextView = (TextView) rootView.findViewById(R.id.tv_time);


            amount.setText(waste.amount.toString());
            userNote.setText(waste.userNote);
            mDateTextView.setText(waste.dayAdded);
            mTimeTextView.setText(waste.timeAdded);
            mSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        waste.amount = Float.parseFloat(amount.getText().toString());
                    } catch (Exception e) {
                        Log.e("logtag", "Exception: " + e.toString());
                    }
                    waste.userNote = userNote.getText().toString();
                    //TODO get selected category
                    waste.category = "Default";
                    waste.dayAdded = mDateTextView.getText().toString();
                    waste.timeAdded = mTimeTextView.getText().toString();
                    waste.save();
                    Intent intent = new Intent(mContext, Statistic.class);
                    startActivity(intent);
                }
            });
            mDateTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDate();
                }
            });
            mTimeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTime();
                }
            });
            return rootView;
        }

        private void updateTimeView() {
            this.mTimeTextView.setText(Config.timeStringBuilder(mMyTimePickerDialog.pHour, mMyTimePickerDialog.pMinute));
        }

        private void updateDateView() {
            this.mDateTextView.setText(Config.dateStringBuilder(mMyDatePickerDialog.pDay, mMyDatePickerDialog.pMonth, mMyDatePickerDialog.pYear));
        }

        public void onEvent(EventBusMessage event) {
            switch (event.dialogID) {
                case MyDatePickerDialog.DIALOG_ID:
                    updateDateView();
                    break;
                case MyTimePickerDialog.DIALOG_ID:
                    updateTimeView();
                    break;
            }
        }

        public void setDate() {
            mMyDatePickerDialog.show(getActivity().getSupportFragmentManager(), "datePicker");
        }

        public void setTime() {
            mMyTimePickerDialog.show(getActivity().getSupportFragmentManager(), "timePicker");
        }

        @Override
        public void onStop() {
            super.onStop();
            EventBus.getDefault().unregister(this);
        }
    }
}
