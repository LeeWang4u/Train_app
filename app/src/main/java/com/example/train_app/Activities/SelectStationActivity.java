package com.example.train_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.train_app.R;

public class SelectStationActivity extends AppCompatActivity {

    private ListView lvStations;
    private String[] stations = {
            "Hà Nội (HNO)", "Sài Gòn (SGO)", "Đà Nẵng (DNA)", "Nha Trang (NTR)",
            "Huế (HUE)", "Vinh (VIN)", "Quảng Ngãi (QNG)", "Tuy Hòa (THA)",
            "Hải Phòng (HPH)", "Lào Cai (LCA)"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_station);

        lvStations = findViewById(R.id.lvStations);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stations);
        lvStations.setAdapter(adapter);

        lvStations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedStation = stations[position];
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedStation", selectedStation);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}