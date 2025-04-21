package com.example.train_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.train_app.R;
import com.example.train_app.model.Station;

import java.util.List;

public class StationAdapter extends ArrayAdapter<Station> {

    public StationAdapter(Context context, int resource, List<Station> stations) {
        super(context, resource, stations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.station_list_item, parent, false);
        }

        Station station = getItem(position);

        TextView tvStationName = convertView.findViewById(R.id.tvStationName);
        TextView tvStationLocation = convertView.findViewById(R.id.tvStationLocation);

        tvStationName.setText(station.getStationName());
        tvStationLocation.setText(station.getLocation());

        return convertView;
    }
}