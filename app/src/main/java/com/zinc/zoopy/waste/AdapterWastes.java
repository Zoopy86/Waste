package com.zinc.zoopy.waste;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;

import java.util.List;
import de.greenrobot.event.EventBus;

/**
 * Created by cat on 28-07-15.
 */
public class AdapterWastes extends ArraySwipeAdapter {
    private final Context mContext;
    private List<Waste> mWastes;
    public AdapterWastes(Context context, List<Waste> objects) {
        super(context, R.layout.item_waste, objects);
        this.mContext = context;
        this.mWastes = objects;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final Waste Waste = mWastes.get(position);
        ViewHolder holder;

        SwipeLayout swipeLayout;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_waste, parent, false);

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
        swipeLayout = (SwipeLayout)convertView.findViewById(R.id.swipe_waste_item);
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout swipeLayout) {

            }

            @Override
            public void onOpen(SwipeLayout swipeLayout) {
                Log.d("Swipe", "On Open");
                swipeLayout.open();
            }

            @Override
            public void onStartClose(SwipeLayout swipeLayout) {

            }

            @Override
            public void onClose(SwipeLayout swipeLayout) {
                Log.d("Swipe", "On Close");
            }

            @Override
            public void onUpdate(SwipeLayout swipeLayout, int i, int i1) {

            }

            @Override
            public void onHandRelease(SwipeLayout swipeLayout, float v, float v1) {

            }
        });
        holder.tvAmount.setText(""+ Waste.amount);
        holder.tvUserNote.setText(Waste.category + ": " + Waste.userNote);
        holder.tvTimeAdded.setText(Waste.dayAdded + " " + Waste.timeAdded);
        holder.bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityEditWaste.class);
                intent.putExtra("id", Waste.getId());
                mContext.startActivity(intent);
                ((Activity)mContext).overridePendingTransition(R.anim.move_left, R.anim.move_left2);
            }
        });

        holder.bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Waste.delete();
                remove(mWastes.get(position));
                notifyDataSetChanged();
                EventBus.getDefault().post(new EventDialog());
            }
        });
        return convertView;
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe_waste_item;
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
}
