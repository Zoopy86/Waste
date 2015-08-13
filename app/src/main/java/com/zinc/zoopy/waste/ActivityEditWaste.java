package com.zinc.zoopy.waste;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.greenrobot.event.EventBus;


public class ActivityEditWaste extends AppCompatActivity {

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
        // Handle action bar item_waste clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_new_waste){
            Intent intent = new Intent(this, ActivityMain.class);
            startActivity(intent);
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
        Button mCategoryButton;
        DateDialog mDateDialog = new DateDialog();
        TimeDialog mTimeDialog = new TimeDialog();

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            EventBus.getDefault().register(this);
            final View rootView = inflater.inflate(R.layout.fragment_edit_waste, container, false);
            amount = (EditText) rootView.findViewById(R.id.ewa_et_amount);
            amount.setInputType(InputType.TYPE_NULL);
            userNote = (EditText) rootView.findViewById(R.id.ewa_et_usernote);
            mSaveButton = (Button) rootView.findViewById(R.id.ewa_b_save);
            mContext = rootView.getContext();
            mDateTextView = (TextView) rootView.findViewById(R.id.tv_date);
            mTimeTextView = (TextView) rootView.findViewById(R.id.tv_time);
            mCategoryButton = (Button)rootView.findViewById(R.id.b_pick_category);

            mCategoryButton.setText(waste.category);
            amount.setText(waste.amount.toString());
            userNote.setText(waste.userNote);
            mDateTextView.setText(waste.dayAdded);
            mTimeTextView.setText(waste.timeAdded);
            mCategoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ActivityPickCategory.class);
                    startActivity(intent);
                    ((Activity)mContext).overridePendingTransition(R.anim.move_left, R.anim.move_left2);
                }
            });
            mSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        waste.amount = Float.parseFloat(amount.getText().toString());
                    } catch (Exception e) {
                        Log.e("logtag", "Exception: " + e.toString());
                    }
                    waste.userNote = userNote.getText().toString();
                    waste.category = mCategoryButton.getText().toString();
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
            this.mTimeTextView.setText(Config.timeStringBuilder(mTimeDialog.pHour, mTimeDialog.pMinute));
        }

        private void updateDateView() {
            this.mDateTextView.setText(Config.dateStringBuilder(mDateDialog.pDay, mDateDialog.pMonth, mDateDialog.pYear));
        }

        public void onEvent(EventBusDialogMessage event) {
            switch (event.dialogID) {
                case DateDialog.DIALOG_ID:
                    updateDateView();
                    break;
                case TimeDialog.DIALOG_ID:
                    updateTimeView();
                    break;
            }
        }
        public void onEvent(EventBusCategoryMessage event) {
            mCategoryButton.setText(event.category);
        }

        public void setDate() {
            mDateDialog.show(getActivity().getSupportFragmentManager(), "datePicker");
        }

        public void setTime() {
            mTimeDialog.show(getActivity().getSupportFragmentManager(), "timePicker");
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            EventBus.getDefault().unregister(this);
        }
    }
}
