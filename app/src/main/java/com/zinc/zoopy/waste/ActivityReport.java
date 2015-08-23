package com.zinc.zoopy.waste;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;

public class ActivityReport extends AppCompatActivity {
    RelativeLayout periodLayout;
    RelativeLayout categoryLayout;
    RelativeLayout resultLayout;
    TextView tvPeriod;
    TextView tvCategory;
    TextView tvResult;
    String mPeriod;
    String[] mCategories;
    List<Waste> dateRecs;
    List<Waste> catsRecs;
    List<Waste> resultRecs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initInstances();

    }

    @Override
    protected void onResume() {
        super.onResume();
        tvResult.setText("" + Waste.getSum(resultRecs));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initInstances() {
        EventBus.getDefault().register(this);
        periodLayout = (RelativeLayout) findViewById(R.id.periodLayout);
        categoryLayout = (RelativeLayout) findViewById(R.id.categoryLayout);
        resultLayout = (RelativeLayout) findViewById(R.id.resultLayout);
        tvPeriod = (TextView) findViewById(R.id.tv_period);
        tvCategory = (TextView) findViewById(R.id.tv_category);
        tvResult = (TextView) findViewById(R.id.tv_result);
        if (Config.reportPeriod != null) {
            mPeriod = Config.reportPeriod;
        } else mPeriod = tvPeriod.getText().toString();
        if (Config.selectedCategories != null) {
            mCategories = Config.selectedCategories;
        } else {
            mCategories = Category.getAllInArray();
        }
        if (mPeriod != null) {
            tvPeriod.setText(mPeriod);
        }

        dateRecs = getRecordsByPeriod(mPeriod);
        catsRecs = getRecordsByCategory(mCategories);
        resultRecs = getRetainedRecords(dateRecs, catsRecs);

        setTvCategoryText();
        tvResult.setText("" + Waste.getSum(resultRecs));

        periodLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityReport.this, ActivityPeriod.class);
                intent.putExtra("period", tvPeriod.getText().toString());
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.move_left, R.anim.move_left2);
            }
        });
        categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityReport.this, ActivityPickCategories.class);
                intent.putExtra("selectedCategories", mCategories);
                startActivityForResult(intent, 2);
            }
        });
        resultLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityReport.this, ActivitySortedEntries.class);
                Config.wastesToShow = resultRecs;
                startActivity(intent);
            }
        });
    }

    private List<Waste> getRecordsByPeriod(String period) {
        Calendar c = Calendar.getInstance(Locale.UK);
        String today = Config.dateStringBuilder(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).toString();
        //TODO: create constants for string values
        switch (period) {
            case "All time":
                return Waste.getAll();
            case "Today":
                return Waste.getBetweenDate(today, today);
            case "This week":
                c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
                String week = Config.dateStringBuilder(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).toString();
                return Waste.getBetweenDate(week, today);
            case "This month":
                c.set(Calendar.DAY_OF_MONTH, 1);
                String thisMonth = Config.dateStringBuilder(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).toString();
                return Waste.getBetweenDate(thisMonth, today);
            case "Last month":
                c.set(Calendar.DAY_OF_MONTH, 1);
                c.add(Calendar.MONTH, -1);
                String lastMonthFirstDay = Config.dateStringBuilder(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).toString();
                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                String lastMonthLastDay = Config.dateStringBuilder(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).toString();
                return Waste.getBetweenDate(lastMonthFirstDay, lastMonthLastDay);
            case "This year":
                c.set(Calendar.DAY_OF_YEAR, 1);
                String thisYear = Config.dateStringBuilder(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).toString();
                return Waste.getBetweenDate(thisYear, today);
            case "Last year":
                c.set(Calendar.DAY_OF_YEAR, 1);
                c.add(Calendar.YEAR, -1);
                String lastYearFirstDay = Config.dateStringBuilder(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).toString();
                c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR));
                String lastYearLastDay = Config.dateStringBuilder(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).toString();
                return Waste.getBetweenDate(lastYearFirstDay, lastYearLastDay);
            default:
                return Waste.getAll();
        }
    }

    private List<Waste> getRecordsByCategory(String... cats) {
        return Waste.getWhere(cats);
    }

    private List<Waste> getRetainedRecords(List<Waste> list1, List<Waste> list2) {
        if (list1.size() < list2.size()) {
            list2.retainAll(list1);
            return list2;
        } else {
            list1.retainAll(list2);
            return list1;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                mPeriod = data.getStringExtra("period");
                if (mPeriod != null) {
                    tvPeriod.setText(mPeriod);

                }
            }
//            if (resultCode == RESULT_CANCELED) {
//                //Write your code if there's no result
//            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                if (data.getStringArrayExtra("selectedCategories") != null) {
                    mCategories = data.getStringArrayExtra("selectedCategories");
                }
                setTvCategoryText();
            }
//            if (resultCode == RESULT_CANCELED) {
//                //If there's no result
//            }
        }
        catsRecs = getRecordsByCategory(mCategories);
        dateRecs = getRecordsByPeriod(mPeriod);
        resultRecs = getRetainedRecords(dateRecs, catsRecs);
    }

    private void setTvCategoryText() {
        if (mCategories.length == Category.getAllInArray().length) {
            tvCategory.setText(R.string.all);
        } else
            tvCategory.setText("" + (mCategories.length) + getString(R.string.of) + (Category.getAllInArray().length));
    }

    public void onEvent(EventPeriod message) {
        tvPeriod.setText(message.period);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_report, menu);
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
