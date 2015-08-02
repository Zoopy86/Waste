package com.zinc.zoopy.waste;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.SwipeListView;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 02-08-15.
 */
public class CategoryAdapter extends ArrayAdapter {
    private final Context mContext;
    private List<Category> mCategories;
    public CategoryAdapter(Context context, List<Category> categories) {
        super(context, R.layout.category_item, categories);
        this.mContext = context;
        this.mCategories = categories;
        EventBus.getDefault().register(this);
    }
    public void onEvent(EventBusMessage event){
        notifyDataSetChanged();
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        final Category category = mCategories.get(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.category_item, parent, false);

            holder = new ViewHolder();
            holder.tvCategory = (TextView)convertView.findViewById(R.id.tv_category);
            holder.bEdit = (Button)convertView.findViewById(R.id.b_edit);
            holder.bDelete = (Button)convertView.findViewById(R.id.b_delete);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        ((SwipeListView)parent).closeAnimate(position);
        holder.tvCategory.setText(category.name);

        //TODO Listeners
        holder.tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("category_name", category.name);
                mContext.startActivity(intent);
                Toast.makeText(mContext, "Picked " + category.name + " ID: " + category.getId(), Toast.LENGTH_LONG).show();
            }
        });
        holder.bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditCategory.class);
                intent.putExtra("category_id", category.getId());
                mContext.startActivity(intent);
                //Toast.makeText(mContext, "Edit " + category.name + " clicked", Toast.LENGTH_LONG).show();
            }
        });
        holder.bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category.delete();
                ((SwipeListView)parent).closeAnimate(position);
                ((SwipeListView)parent).dismiss(position);
                remove(mCategories.get(position));
                notifyDataSetChanged();

                //Toast.makeText(mContext, "Delete " + category.name + " clicked", Toast.LENGTH_LONG).show();
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView tvCategory;
        Button bEdit;
        Button bDelete;
        public ViewHolder(){}
    }

}
