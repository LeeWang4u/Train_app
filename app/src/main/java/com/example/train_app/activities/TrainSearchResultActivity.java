package com.example.train_app.activities;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.train_app.R;
import com.example.train_app.adapters.recyclerView.TrainAdapter;
import com.example.train_app.model.Trip;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TrainSearchResultActivity extends AppCompatActivity {
    private RecyclerView rvTrainResults;
    private TextView tvRoute, tvDate;
    private List<Trip> trips = new ArrayList<>();
    private TrainAdapter adapter;

    private LocalDate currentTripDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_search_result);

        rvTrainResults = findViewById(R.id.rvTrainResults);
        tvRoute = findViewById(R.id.tvRoute);
        tvDate = findViewById(R.id.tvDate);

        // Lấy tham số từ Intent
        String departureStation = getIntent().getStringExtra("departureStation") != null ? getIntent().getStringExtra("departureStation") : "Hà Nội";
        String arrivalStation = getIntent().getStringExtra("arrivalStation") != null ? getIntent().getStringExtra("arrivalStation") : "Sài Gòn";
//        LocalDate tripDate = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            tripDate = LocalDate.parse(getIntent().getStringExtra("tripDate") != null ? getIntent().getStringExtra("tripDate") : "2025-05-08");
//        }
//        tvRoute.setText(departureStation + " - " + arrivalStation);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            tvDate.setText(tripDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentTripDate = LocalDate.parse(getIntent().getStringExtra("tripDate") != null ? getIntent().getStringExtra("tripDate") : "2025-05-08");
        }

        tvRoute.setText(departureStation + " - " + arrivalStation);
        updateDateViews(); // Cập nhật giao diện ngày


       // updateDateViews(); // Cập nhật giao diện ngày

        // Lấy danh sách chuyến tàu giả lập
        //trips.addAll(fetchTrips(departureStation, arrivalStation, tripDate));
        trips.addAll(fetchTrips(departureStation, arrivalStation, currentTripDate));

        adapter = new TrainAdapter(trips, trip -> {
            Intent intent = new Intent(TrainSearchResultActivity.this, TrainDetailActivity.class);
            intent.putExtra("tripId", trip.getTripId());
            intent.putExtra("departureStation", trip.getDepartureStation());
            intent.putExtra("arrivalStation", trip.getArrivalStation());
            startActivity(intent);
        });
        rvTrainResults.setLayoutManager(new LinearLayoutManager(this));
        rvTrainResults.setAdapter(adapter);

        // Xử lý nút điều hướng
//        findViewById(R.id.btnPrevDate).setOnClickListener(v -> updateDate(-1));
//        findViewById(R.id.btnNextDate).setOnClickListener(v -> updateDate(1));
//        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnPrevDate).setOnClickListener(v -> changeDate(-1));
        findViewById(R.id.tvPreviousDate).setOnClickListener(v -> changeDate(-1));
        findViewById(R.id.btnNextDate).setOnClickListener(v -> changeDate(1));
        findViewById(R.id.tvNextDate).setOnClickListener(v -> changeDate(1));

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void updateDate(int days) {
        LocalDate currentDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentDate = LocalDate.parse(tvDate.getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        LocalDate newDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            newDate = currentDate.plusDays(days);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tvDate.setText(newDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        trips.clear();
        trips.addAll(fetchTrips(getIntent().getStringExtra("departureStation"), getIntent().getStringExtra("arrivalStation"), newDate));
        adapter.notifyDataSetChanged();
    }

    private void changeDate(int offsetDays) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentTripDate = currentTripDate.plusDays(offsetDays);
            updateDateViews();

            trips.clear();
            trips.addAll(fetchTrips(getIntent().getStringExtra("departureStation"),
                    getIntent().getStringExtra("arrivalStation"),
                    currentTripDate));
            adapter.notifyDataSetChanged();
        }
    }

    private void updateDateViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            TextView tvPrev = findViewById(R.id.tvPreviousDate);
            TextView tvNext = findViewById(R.id.tvNextDate);

            tvDate.setText(currentTripDate.format(formatter));
            tvPrev.setText(currentTripDate.minusDays(1).format(formatter));
            tvNext.setText(currentTripDate.plusDays(1).format(formatter));
        }
    }



    private List<Trip> fetchTrips(String departureStation, String arrivalStation, LocalDate tripDate) {
        List<Trip> tripList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tripList.add(new Trip(1, "SE7", new BigDecimal("500000"), tripDate, "ON_TIME", departureStation, arrivalStation,
                    LocalDateTime.of(tripDate, LocalTime.of(6, 10)),
                    LocalDateTime.of(tripDate.plusDays(1), LocalTime.of(18, 10)), 174));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tripList.add(new Trip(2, "SE5", new BigDecimal("550000"), tripDate, "ON_TIME", departureStation, arrivalStation,
                    LocalDateTime.of(tripDate, LocalTime.of(15, 30)),
                    LocalDateTime.of(tripDate.plusDays(1), LocalTime.of(5, 18)), 176));
        }
        return tripList;
    }
}