package com.zinc.zoopy.waste;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 31-08-15.
 */
public class FragmentJournal extends Fragment{
    ListView mListView;
    AdapterJournalMonths mAdapterJournalMonths;
    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View rootView = inflater.inflate(R.layout.activity_journal, container, false);
        mContext = getActivity().getApplicationContext();
        mListView = (ListView)rootView.findViewById(R.id.journal_list_dates);
        mAdapterJournalMonths = new AdapterJournalMonths(mContext, Waste.groupByYearAndMonth());
        mListView.setAdapter(mAdapterJournalMonths);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
                Waste waste = (Waste)mListView.getItemAtPosition(position);
                Intent intent = new Intent(mContext, ActivityJournalMonths.class);
                intent.putExtra("date", waste.dayAdded);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.move_left, R.anim.move_left2);

            }
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(EventSimple event){
        mAdapterJournalMonths.notifyDataSetChanged();
        mAdapterJournalMonths = new AdapterJournalMonths(mContext, Waste.groupByYearAndMonth());
        mListView.setAdapter(mAdapterJournalMonths);
        Log.d("Simple Event", "Event fired");
    }
}
