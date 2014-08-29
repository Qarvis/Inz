package com.project.my.inz.adapter;

import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.my.inz.Model.DataModel;
import com.project.my.inz.app.R;


public class AchiveArrayAdapter extends ArrayAdapter<DataModel> {
    private final LayoutInflater mInflater;
    Context context;

    public AchiveArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    public void setData(List<DataModel> data) {
        clear();
        if (data != null) {
            for (DataModel appEntry : data) {
                add(appEntry);
            }
        }
    }

    /**
     * Populate new items in the list.
     */
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        DataModel item = getItem(position);

        if (convertView == null) {
            view = mInflater.inflate(R.layout.achive_list_item, parent, false);
        }else {
            view = convertView;
        }

        LinearLayout Layout = (LinearLayout) view.findViewById(R.id.achive_content);
        int state =  Integer.parseInt(item.getState());

        if(state==11) {// set colors

            Layout.setBackgroundColor(Color.parseColor("#ffaf867e"));
       }else{
            Layout.setBackgroundColor(Color.parseColor("#ff7caf63"));
        }

        ((TextView) view.findViewById(R.id.achive_label)).setText(item.getName());
        ((TextView) view.findViewById(R.id.achive_points)).setText(item.getPoints());
        ((TextView) view.findViewById(R.id.achive_desc)).setText(item.getDesc());


        view.setOnClickListener(null);
        view.setOnLongClickListener(null);
        view.setLongClickable(false);

        return view;
    }
}