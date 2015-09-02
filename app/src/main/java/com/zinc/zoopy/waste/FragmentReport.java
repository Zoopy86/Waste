package com.zinc.zoopy.waste;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 31-08-15.
 */
public class FragmentReport extends Fragment {
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
    Context mContext;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_report, container, false);
        mContext = getActivity().getApplicationContext();
        initInstances();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void initInstances() {
        EventBus.getDefault().register(this);
        periodLayout = (RelativeLayout) rootView.findViewById(R.id.periodLayout);
        categoryLayout = (RelativeLayout) rootView.findViewById(R.id.categoryLayout);
        resultLayout = (RelativeLayout) rootView.findViewById(R.id.resultLayout);
        tvPeriod = (TextView) rootView.findViewById(R.id.tv_period);
        tvCategory = (TextView) rootView.findViewById(R.id.tv_category);
        tvResult = (TextView) rootView.findViewById(R.id.tv_result);
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

        dateRecs = Waste.getRecordsByPeriod(mPeriod);
        catsRecs = Waste.getRecordsByCategory(mCategories);
        resultRecs = Waste.getRetainedRecords(dateRecs, catsRecs);

        setTvCategoryText();
        tvResult.setText("" + Waste.getSum(resultRecs));

        periodLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActivityPeriod.class);
                intent.putExtra("period", tvPeriod.getText().toString());
                startActivityForResult(intent, 1);
                getActivity().overridePendingTransition(R.anim.move_left, R.anim.move_left2);
            }
        });
        categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActivityPickCategories.class);
                intent.putExtra("selectedCategories", mCategories);
                startActivityForResult(intent, 2);
                getActivity().overridePendingTransition(R.anim.move_left, R.anim.move_left2);
            }
        });
        resultLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActivitySortedEntries.class);
                Config.wastesToShow = resultRecs;
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.move_left, R.anim.move_left2);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        tvResult.setText("" + Waste.getSum(resultRecs));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == FragmentActivity.RESULT_OK) {
                mPeriod = data.getStringExtra("period");
                if (mPeriod != null) {
                    tvPeriod.setText(mPeriod);

                }
            }
        }
        if (requestCode == 2) {
            if (resultCode == FragmentActivity.RESULT_OK) {
                if (data.getStringArrayExtra("selectedCategories") != null) {
                    mCategories = data.getStringArrayExtra("selectedCategories");
                }
                setTvCategoryText();
            }
        }
        catsRecs = Waste.getRecordsByCategory(mCategories);
        dateRecs = Waste.getRecordsByPeriod(mPeriod);
        resultRecs = Waste.getRetainedRecords(dateRecs, catsRecs);
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
}
