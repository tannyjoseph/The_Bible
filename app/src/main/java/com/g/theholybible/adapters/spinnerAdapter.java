package com.g.theholybible.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.g.theholybible.R;

public class spinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    String[] str;
    Context context;
    String[] colors = {"#13edea","#e20ecd","#15ea0d", "#13edea", "#13edea"};
    String[] colorsback = {"#FF000000","#FFF5F1EC","#ea950d", "#13edea","#13edea"};


    public spinnerAdapter(Context context, String[] str) {

        this.context = context;
        this.str = str;

    }

    @Override
    public int getCount() {
        return str.length;
    }

    @Override
    public Object getItem(int position) {
        return str[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  View.inflate(context, R.layout.spin, null);
        TextView textView = (TextView) view.findViewById(R.id.main);
        textView.setTextColor(view.getResources().getColor(R.color.WHITE));
        textView.setText(str[position]);
        return textView;

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view;
        view =  View.inflate(context, R.layout.spindrop, null);
        final TextView textView = (TextView) view.findViewById(R.id.dropdown);
        textView.setText(str[position]);

        textView.setTextColor(Color.parseColor(colors[position]));
        textView.setBackgroundColor(Color.parseColor(colorsback[position]));


        return view;
    }
}
