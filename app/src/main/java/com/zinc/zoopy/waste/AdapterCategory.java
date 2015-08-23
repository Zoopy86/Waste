package com.zinc.zoopy.waste;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Zoopy86 on 02-08-15.
 */
public class AdapterCategory extends ArrayAdapter{

    private final Context mContext;
    private List<Category> mCategories;

    public AdapterCategory(Context context, List<Category> categories) {
        super(context, R.layout.item_category, categories);
        this.mContext = context;
        this.mCategories = categories;
        EventBus.getDefault().register(this);
    }

    public void onEvent(EventDialog event) {
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        final Category Category = mCategories.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_category, parent, false);

            holder = new ViewHolder();
            holder.tvCategory = (TextView) convertView.findViewById(R.id.tv_category);
            holder.bEdit = (Button) convertView.findViewById(R.id.b_edit);
            holder.bDelete = (Button) convertView.findViewById(R.id.b_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvCategory.setText(Category.name);

        holder.tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventCategory(Category.name));
                ((Activity) mContext).finish();
                ((Activity) mContext).overridePendingTransition(R.anim.move_right2, R.anim.move_right);
            }
        });
        holder.bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActivityEditCategory.class);
                intent.putExtra("category_id", Category.getId());
                mContext.startActivity(intent);
            }
        });
        holder.bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Category.delete();
                remove(mCategories.get(position));
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView tvCategory;
        Button bEdit;
        Button bDelete;

        public ViewHolder() {
        }
    }

}
