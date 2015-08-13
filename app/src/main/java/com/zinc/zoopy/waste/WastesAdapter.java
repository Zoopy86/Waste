package com.zinc.zoopy.waste;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 28-07-15.
 */
public class WastesAdapter extends ArrayAdapter {
    private final Context mContext;
    private List<Waste> mWastes;
    public WastesAdapter(Context context, List<Waste> objects) {
        super(context, R.layout.item_waste, objects);
        this.mContext = context;
        this.mWastes = objects;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final Waste waste = mWastes.get(position);
        ViewHolder holder;

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


        holder.tvAmount.setText(""+ waste.amount);
        holder.tvUserNote.setText(waste.category + ": " + waste.userNote);
        holder.tvTimeAdded.setText(waste.dayAdded + " " + waste.timeAdded);
        holder.bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityEditWaste.class);
                intent.putExtra("id", waste.getId());
                mContext.startActivity(intent);
            }
        });

        holder.bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waste.delete();
                remove(mWastes.get(position));
                notifyDataSetChanged();
                EventBus.getDefault().post(new EventBusDialogMessage());
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
}
