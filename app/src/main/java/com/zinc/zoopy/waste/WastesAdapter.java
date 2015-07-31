package com.zinc.zoopy.waste;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.fortysevendeg.swipelistview.SwipeListView;

import java.util.Collection;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 28-07-15.
 */
public class WastesAdapter extends ArrayAdapter {
    private final Context mContext;
    private List<Waste> mWastes;
    public WastesAdapter(Context context, List<Waste> objects) {
        super(context, R.layout.item, objects);
        this.mContext = context;
        this.mWastes = objects;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final Waste waste = mWastes.get(position);
        ViewHolder holder;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item, parent, false);

            holder = new ViewHolder();
            holder.tvAmount = (TextView) convertView.findViewById(R.id.amount);
            holder.tvUserNote = (TextView) convertView.findViewById(R.id.userNote);
            holder.tvTimeAdded = (TextView) convertView.findViewById(R.id.timeAdded);
            holder.bEdit = (Button)convertView.findViewById(R.id.b_edit);
            holder.bDelete = (Button)convertView.findViewById(R.id.b_delete);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.icon);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        ((SwipeListView)parent).recycle(convertView, position);

        holder.tvAmount.setText(""+ waste.amount + " " + waste.currency);
        holder.tvUserNote.setText(waste.category + ": " + waste.userNote);
        holder.tvTimeAdded.setText(waste.timeAdded);
        holder.bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO make edit waste activity
                Intent intent = new Intent(mContext, EditWasteActivity.class);
                intent.putExtra("id", waste.getId());
                intent.putExtra("amount", waste.amount);
                intent.putExtra("userNote", waste.userNote);
                mContext.startActivity(intent);
            }
        });

        holder.bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Delete from database
                //((SwipeListView)parent).closeAnimate(position);
                Waste.delete(Waste.class, waste.getId());
                ((SwipeListView)parent).dismiss(position);
                remove(mWastes.get(position));
                notifyDataSetChanged();
                EventBus.getDefault().post(new EventBusMessage());
            }
        });



        return convertView;
    }
    static class ViewHolder {
        ImageView ivImage;
        TextView tvAmount;
        TextView tvUserNote;
        TextView tvTimeAdded;
        Button bEdit;
        Button bDelete;
        public ViewHolder(){}
    }
    @Override
    public void addAll(Collection collection) {
        super.addAll(collection);
        this.mWastes = (List<Waste>) collection;
    }
}
