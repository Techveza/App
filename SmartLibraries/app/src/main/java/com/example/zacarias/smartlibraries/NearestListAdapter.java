package com.example.zacarias.smartlibraries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Zacarias on 21/02/15.
 */


public class NearestListAdapter extends BaseAdapter {

    String[] names;
    Context context;
    int count;

    public NearestListAdapter(Context context, String[] names) {
        this.context = context;
        this.names = names;
        count = 4;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void more() {
        if (count + 2 < names.length) {
            count = count + 3;
        } else {
            count = names.length + 1;
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == count - 1) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_button, parent, false);

        } else {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_nearest, parent, false);


            TextView name = (TextView) convertView.findViewById(R.id.biblio_name);
            name.setText(names[position]);
        }

        return convertView;
    }

}
