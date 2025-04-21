package com.example.train_app.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.train_app.model.Station;

import java.util.List;

public class StationAdapter extends ArrayAdapter<Station> {

    public StationAdapter(Context context, int resource, List<Station> stations) {
        super(context, resource, stations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view;
        Station station = getItem(position);
        // Hiển thị theo định dạng "stationName - location"
        textView.setText(station.getStationName() + " - " + station.getLocation());
        return view;
    }
}