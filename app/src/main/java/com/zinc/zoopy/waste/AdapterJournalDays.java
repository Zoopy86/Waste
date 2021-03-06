package com.zinc.zoopy.waste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Zoopy86 on 12-08-15.
 */
public class AdapterJournalDays extends ArrayAdapter {
    private final Context mContext;
    private List<Waste> mWasteDates;
    public AdapterJournalDays(Context context, List<Waste> objects) {
        super(context,R.layout.item_journal_month, objects);
        this.mContext = context;
        this.mWasteDates = objects;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Waste wasteDate = mWasteDates.get(position);
        ViewHolder holder;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_journal_month, parent, false);
            holder = new ViewHolder();
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date_month);
            holder.tvEntries = (TextView) convertView.findViewById(R.id.tv_entries);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tvDate.setText("" + Config.dayFormat(wasteDate.dayAdded));
        holder.tvEntries.setText(mContext.getString(R.string.entries) + ": " + Waste.getByDay(wasteDate.dayAdded).size() +" " + mContext.getString(R.string.sum) +": " + Waste.getSum(Waste.getByDay(wasteDate.dayAdded)));
        return convertView;
    }

    static class ViewHolder {
        TextView tvDate;
        TextView tvEntries;
        public ViewHolder(){}
    }
}
