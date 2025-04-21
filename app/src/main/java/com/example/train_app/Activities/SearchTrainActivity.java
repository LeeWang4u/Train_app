package com.example.train_app.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.train_app.R;

import java.util.Calendar;

public class SearchTrainActivity extends AppCompatActivity {

    private Button btnDepartureStation, btnArrivalStation, btnDepartureDate, btnSearchTrain;
    private String selectedDepartureStation, selectedArrivalStation, selectedDate;
    private static final int REQUEST_DEPARTURE_STATION = 1;
    private static final int REQUEST_ARRIVAL_STATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_train);

        btnDepartureStation = findViewById(R.id.btnDepartureStation);
        btnArrivalStation = findViewById(R.id.btnArrivalStation);
        btnDepartureDate = findViewById(R.id.btnDepartureDate);
        btnSearchTrain = findViewById(R.id.btnSearchTrain);

        // Select Departure Station
        btnDepartureStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchTrainActivity.this, SelectStationActivity.class);
                startActivityForResult(intent, REQUEST_DEPARTURE_STATION);
            }
        });

        // Select Arrival Station
        btnArrivalStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchTrainActivity.this, SelectStationActivity.class);
                startActivityForResult(intent, REQUEST_ARRIVAL_STATION);
            }
        });

        // Select Departure Date
        btnDepartureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SearchTrainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                btnDepartureDate.setText(selectedDate);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // Search Train
        btnSearchTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDepartureStation == null || selectedArrivalStation == null || selectedDate == null) {
                    Toast.makeText(SearchTrainActivity.this, "Vui lòng chọn đầy đủ ga đi, ga đến và ngày đi", Toast.LENGTH_SHORT).show();
                } else if (selectedDepartureStation.equals(selectedArrivalStation)) {
                    Toast.makeText(SearchTrainActivity.this, "Ga đi và ga đến không được trùng nhau", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SearchTrainActivity.this, "Tìm chuyến tàu từ " + selectedDepartureStation + " đến " + selectedArrivalStation + " vào ngày " + selectedDate, Toast.LENGTH_LONG).show();
                    // Logic tìm chuyến tàu có thể được thêm ở đây (gọi API hoặc truy vấn cơ sở dữ liệu)
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String selectedStation = data.getStringExtra("selectedStation");
            if (requestCode == REQUEST_DEPARTURE_STATION) {
                selectedDepartureStation = selectedStation;
                btnDepartureStation.setText(selectedStation);
            } else if (requestCode == REQUEST_ARRIVAL_STATION) {
                selectedArrivalStation = selectedStation;
                btnArrivalStation.setText(selectedStation);
            }
        }
    }
}