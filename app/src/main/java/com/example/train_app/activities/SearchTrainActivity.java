package com.example.train_app.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.train_app.R;
import com.example.train_app.api.ApiService;
import com.example.train_app.api.HTTPService;
import com.example.train_app.model.Station;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTrainActivity extends AppCompatActivity {

    private Button btnDepartureStation, btnArrivalStation, btnDepartureDate, btnSearchTrain;
    private String selectedDepartureStation, selectedArrivalStation, selectedDate;
    private static final int REQUEST_DEPARTURE_STATION = 1;
    private static final int REQUEST_ARRIVAL_STATION = 2;
    private List<String> stationNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_train);

        // Khởi tạo các view
        btnDepartureStation = findViewById(R.id.btnDepartureStation);
        btnArrivalStation = findViewById(R.id.btnArrivalStation);
        btnDepartureDate = findViewById(R.id.btnDepartureDate);
        btnSearchTrain = findViewById(R.id.btnSearchTrain);

        // Gọi API để lấy danh sách ga
        fetchStations();

        System.out.print("Hellooooooooooooooo");

        // Chọn ga đi
        btnDepartureStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchTrainActivity.this, SelectStationActivity.class);
                startActivityForResult(intent, REQUEST_DEPARTURE_STATION);
            }
        });

        // Chọn ga đến
        btnArrivalStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchTrainActivity.this, SelectStationActivity.class);
                startActivityForResult(intent, REQUEST_ARRIVAL_STATION);
            }
        });

        // Chọn ngày đi
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

        // Tìm kiếm chuyến tàu
        btnSearchTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDepartureStation == null || selectedArrivalStation == null || selectedDate == null) {
                    Toast.makeText(SearchTrainActivity.this, "Vui lòng chọn đầy đủ ga đi, ga đến và ngày đi", Toast.LENGTH_SHORT).show();
                } else if (selectedDepartureStation.equals(selectedArrivalStation)) {
                    Toast.makeText(SearchTrainActivity.this, "Ga đi và ga đến không được trùng nhau", Toast.LENGTH_SHORT).show();
                } else if (!stationNames.contains(selectedDepartureStation) || !stationNames.contains(selectedArrivalStation)) {
                    Toast.makeText(SearchTrainActivity.this, "Ga đi hoặc ga đến không hợp lệ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SearchTrainActivity.this, "Tìm chuyến tàu từ " + selectedDepartureStation + " đến " + selectedArrivalStation + " vào ngày " + selectedDate, Toast.LENGTH_LONG).show();
                    // Logic tìm chuyến tàu có thể được thêm ở đây (gọi API hoặc truy vấn cơ sở dữ liệu)
                }
            }
        });
    }

    private void fetchStations() {
        // Khởi tạo ApiService
        ApiService apiService = HTTPService.getInstance().create(ApiService.class);

        // Gọi API
        Call<List<Station>> call = apiService.getAllStations();
        call.enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    stationNames.clear();
                    for (Station station : response.body()) {
                        stationNames.add(station.getStationName());
                    }
                } else {
                    Toast.makeText(SearchTrainActivity.this, "Lỗi khi lấy danh sách ga", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                System.out.print(t.getMessage());
                Toast.makeText(SearchTrainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

//package com.example.train_app.activities;
//
//import android.app.DatePickerDialog;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.train_app.R;
//import com.example.train_app.api.ApiService;
//import com.example.train_app.api.HTTPService;
//import com.example.train_app.model.Station;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class SearchTrainActivity extends AppCompatActivity {
//
//    private AutoCompleteTextView actvDepartureStation, actvArrivalStation;
//    private Button btnDepartureDate, btnSearchTrain;
//    private String selectedDepartureStation, selectedArrivalStation, selectedDate;
//    private List<Station> stationList = new ArrayList<>();
//    private List<String> stationNames = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search_train);
//
//        actvDepartureStation = findViewById(R.id.actvDepartureStation);
//        actvArrivalStation = findViewById(R.id.actvArrivalStation);
//        btnDepartureDate = findViewById(R.id.btnDepartureDate);
//        btnSearchTrain = findViewById(R.id.btnSearchTrain);
//
//        // Gọi API để lấy danh sách ga
//        fetchStations();
//
//        // Select Departure Date
//        btnDepartureDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar calendar = Calendar.getInstance();
//                int year = calendar.get(Calendar.YEAR);
//                int month = calendar.get(Calendar.MONTH);
//                int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(SearchTrainActivity.this,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                                selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
//                                btnDepartureDate.setText(selectedDate);
//                            }
//                        }, year, month, day);
//                datePickerDialog.show();
//            }
//        });
//
//        // Search Train
//        btnSearchTrain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectedDepartureStation = actvDepartureStation.getText().toString().trim();
//                selectedArrivalStation = actvArrivalStation.getText().toString().trim();
//
//                if (selectedDepartureStation.isEmpty() || selectedArrivalStation.isEmpty() || selectedDate == null) {
//                    Toast.makeText(SearchTrainActivity.this, "Vui lòng chọn đầy đủ ga đi, ga đến và ngày đi", Toast.LENGTH_SHORT).show();
//                } else if (selectedDepartureStation.equals(selectedArrivalStation)) {
//                    Toast.makeText(SearchTrainActivity.this, "Ga đi và ga đến không được trùng nhau", Toast.LENGTH_SHORT).show();
//                } else if (!stationNames.contains(selectedDepartureStation) || !stationNames.contains(selectedArrivalStation)) {
//                    Toast.makeText(SearchTrainActivity.this, "Ga đi hoặc ga đến không hợp lệ", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(SearchTrainActivity.this, "Tìm chuyến tàu từ " + selectedDepartureStation + " đến " + selectedArrivalStation + " vào ngày " + selectedDate, Toast.LENGTH_LONG).show();
//                    // Logic tìm chuyến tàu có thể được thêm ở đây (gọi API hoặc truy vấn cơ sở dữ liệu)
//                }
//            }
//        });
//    }
//
//    private void fetchStations() {
//        // Khởi tạo ApiService
//        ApiService apiService = HTTPService.getInstance().create(ApiService.class);
//
//        // Gọi API
//        Call<List<Station>> call = apiService.getAllStations();
//        call.enqueue(new Callback<List<Station>>() {
//            @Override
//            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    stationList.clear();
//                    stationList.addAll(response.body());
//                    stationNames.clear();
//                    for (Station station : stationList) {
//                        stationNames.add(station.getStationName());
//                    }
//
//                    // Thiết lập AutoCompleteTextView
//                    ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchTrainActivity.this,
//                            android.R.layout.simple_dropdown_item_1line, stationNames);
//                    actvDepartureStation.setAdapter(adapter);
//                    actvArrivalStation.setAdapter(adapter);
//                } else {
//                    Toast.makeText(SearchTrainActivity.this, "Lỗi khi lấy danh sách ga", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Station>> call, Throwable t) {
//                Toast.makeText(SearchTrainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
//
//
