package com.project.my.inz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.my.inz.Model.DataModel;
import com.project.my.inz.app.R;

import java.util.List;


public class QuestArrayAdapter extends ArrayAdapter<DataModel> {
    private final LayoutInflater mInflater;

    public QuestArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<DataModel> data) {
        clear();
        if (data != null) {
            for (DataModel appEntry : data) {
                add(appEntry);
            }
        }
    }


    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        DataModel item = getItem(position);

        if (convertView == null) {
            view = mInflater.inflate(R.layout.achive_list_item, parent, false);
        }else {
            view = convertView;
        }
            ((TextView) view.findViewById(R.id.achive_label)).setText(item.getName());
            ((TextView) view.findViewById(R.id.achive_points)).setText(item.getPoints());
            ((TextView) view.findViewById(R.id.achive_desc)).setText(item.getDesc());


        return view;
    }
}