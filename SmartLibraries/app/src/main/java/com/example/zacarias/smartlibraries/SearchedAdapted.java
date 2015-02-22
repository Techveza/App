package com.example.zacarias.smartlibraries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Zacarias on 22/02/15.
 */
public class SearchedAdapted extends BaseAdapter {

    ArrayList<String> names;
    Context context;
    int count;

    public SearchedAdapted(Context context, ArrayList<String> names) {
        this.context = context;
        this.names = names;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_nearest, parent, false);

        }

        TextView name = (TextView) convertView.findViewById(R.id.biblio_name);
        name.setText(names.get(position));


        return convertView;
    }

}

