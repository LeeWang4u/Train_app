package com.example.train_app.activities;

import static com.example.train_app.helper.Format.formatDateToDashYMD;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.train_app.MainActivity;
import com.example.train_app.R;
import com.example.train_app.adapters.recyclerView.RouteAdapter;
import com.example.train_app.api.ApiService;
import com.example.train_app.api.HTTPService;
import com.example.train_app.container.request.TripRequest;
import com.example.train_app.dto.request.TripSeatRequestDTO;
import com.example.train_app.dto.response.RouteBasedTicketCountResponse;
import com.example.train_app.model.Station;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTrainActivity extends AppCompatActivity {

    private Button btnDepartureStation, btnArrivalStation, btnDepartureDate, btnSearchTrain, btnCheckTicket;
    private String selectedDepartureStation, selectedArrivalStation, selectedDate;
    private List<String> stationNames = new ArrayList<>();

    private ActivityResultLauncher<Intent> departureStationLauncher;
    private ActivityResultLauncher<Intent> arrivalStationLauncher;
    private ActivityResultLauncher<Intent> dateLauncher;

    //Tao
    private RecyclerView recyclerView;
    private RouteAdapter adapter;
    private LinearLayoutManager layoutManager;
    private Handler handler = new Handler();
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_train);

        //tao
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

//        List<RouteBasedTicketCountResponse> routes = fetchTicketCountByRoutes();
//        adapter = new RouteAdapter(routes);
//        recyclerView.setAdapter(adapter);
//        startAutoScroll(routes.size());

        btnDepartureStation = findViewById(R.id.btnDepartureStation);
        btnArrivalStation = findViewById(R.id.btnArrivalStation);
        btnDepartureDate = findViewById(R.id.btnDepartureDate);
        btnSearchTrain = findViewById(R.id.btnSearchTrain);
        btnCheckTicket = findViewById(R.id.btnCheckTicket);
        departureStationLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedDepartureStation = result.getData().getStringExtra("selectedStation");
                        btnDepartureStation.setText(selectedDepartureStation);
                    }
                });

        arrivalStationLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedArrivalStation = result.getData().getStringExtra("selectedStation");
                        btnArrivalStation.setText(selectedArrivalStation);
                    }
                });

        dateLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedDate = result.getData().getStringExtra("selected_date");
                        btnDepartureDate.setText(selectedDate);
                    }
                });

        fetchStations();

        btnDepartureStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchTrainActivity.this, SelectStationActivity.class);
                intent.putExtra("stationType", "departure"); // Truyền thông tin chọn ga đi
                departureStationLauncher.launch(intent);
            }
        });

        btnArrivalStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchTrainActivity.this, SelectStationActivity.class);
                intent.putExtra("stationType", "arrival"); // Truyền thông tin chọn ga đến
                arrivalStationLauncher.launch(intent);
            }
        });

        btnDepartureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchTrainActivity.this, SelectDateActivity.class);
                if (selectedDate != null) {
                    intent.putExtra("selected_date", selectedDate);
                }
                dateLauncher.launch(intent);
            }
        });


        btnSearchTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDepartureStation == null || selectedArrivalStation == null || selectedDate == null) {
                    Toast.makeText(SearchTrainActivity.this, "Vui lòng chọn đầy đủ ga đi, ga đến và ngày đi", Toast.LENGTH_SHORT).show();
                } else if (selectedDepartureStation.equals(selectedArrivalStation)) {
                    Toast.makeText(SearchTrainActivity.this, "Ga đi và ga đến không được trùng nhau", Toast.LENGTH_SHORT).show();
                } else if (!stationNames.contains(selectedDepartureStation)) {
                    Toast.makeText(SearchTrainActivity.this, "Ga đi không hợp lệ", Toast.LENGTH_SHORT).show();
                } else if (!stationNames.contains(selectedArrivalStation)) {
                    Toast.makeText(SearchTrainActivity.this, "Ga đến không hợp lệ", Toast.LENGTH_SHORT).show();
                } else {

                    TripRequest tripRequest = new TripRequest(selectedDepartureStation, selectedArrivalStation, formatDateToDashYMD(selectedDate) );
                    Intent intent = new Intent(SearchTrainActivity.this, TrainSearchResultActivity.class);
                    intent.putExtra("tripRequest", tripRequest);
                    startActivity(intent);
                    Toast.makeText(SearchTrainActivity.this, "Tìm chuyến tàu từ " + selectedDepartureStation + " đến " + selectedArrivalStation + " vào ngày " + selectedDate, Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCheckTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khi nút được nhấn, mở SecondActivity
                Intent intent = new Intent(SearchTrainActivity.this, CheckTicketActivity.class);
                startActivity(intent);
            }
        });

        fetchTicketCountByRoutes();
    }

    //tao
    private void startAutoScroll(int size) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPosition = (currentPosition + 1) % size;
                recyclerView.smoothScrollToPosition(currentPosition);
                handler.postDelayed(this, 10000); // 10 seconds
            }
        }, 10000);
    }

    private void fetchTicketCountByRoutes(){
        ApiService apiService = HTTPService.getInstance().create(ApiService.class);
        Call<List<RouteBasedTicketCountResponse>> call = apiService.getTicketCountByRoutes();
        call.enqueue(new Callback<List<RouteBasedTicketCountResponse>>() {
            @Override
            public void onResponse(Call<List<RouteBasedTicketCountResponse>> call, Response<List<RouteBasedTicketCountResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RouteBasedTicketCountResponse> routeList = response.body();
                    Log.d("Routes", "Routes come here ...");
                    List<RouteBasedTicketCountResponse> routes = new ArrayList<>();
                    for (int i = 0; i < routeList.size(); i++) {
                        RouteBasedTicketCountResponse item = routeList.get(i);
                        String rank = String.valueOf(i + 1);
                        routes.add(new RouteBasedTicketCountResponse(item.getFrom(), item.getTo(), item.getTicketsCount()));
                    }

                    runOnUiThread(() -> {
                        adapter = new RouteAdapter(routes);
                        recyclerView.setAdapter(adapter);
                        startAutoScroll(routes.size());
                    });
                }
            }

            @Override
            public void onFailure(Call<List<RouteBasedTicketCountResponse>> call, Throwable t) {
                Log.e("API_ERROR", "Failed to fetch routes", t);
                Toast.makeText(SearchTrainActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchStations() {
        ApiService apiService = HTTPService.getInstance().create(ApiService.class);
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
                    Toast.makeText(SearchTrainActivity.this, "Lỗi khi lấy danh sách ga: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                Toast.makeText(SearchTrainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}