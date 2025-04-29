package com.example.train_app.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.train_app.R;
import com.example.train_app.adapters.recyclerView.TrainAdapter;
import com.example.train_app.api.ApiService;
import com.example.train_app.api.HTTPService;
import com.example.train_app.container.request.TripRequest;
import com.example.train_app.model.Trip;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainSearchResultActivity extends AppCompatActivity {
    private RecyclerView rvTrainResults;
    private TextView tvRoute, tvDate, txtNoTrips;
    private List<Trip> trips = new ArrayList<>();
    private TrainAdapter adapter;
    private LocalDate currentTripDate;
    private TripRequest originalTripRequest; // Lưu TripRequest ban đầu

    private static final DateTimeFormatter ISO_DATE_FORMATTER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
            DateTimeFormatter.ofPattern("yyyy-MM-dd") : null;
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
            DateTimeFormatter.ofPattern("dd/MM/yyyy") : null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_search_result);

        // Khởi tạo views
        rvTrainResults = findViewById(R.id.rvTrainResults);
        tvRoute = findViewById(R.id.tvRoute);
        tvDate = findViewById(R.id.tvDate);
        txtNoTrips = findViewById(R.id.txtNoTrips);

        // Lấy TripRequest từ Intent
        originalTripRequest = (TripRequest) getIntent().getSerializableExtra("tripRequest");
        if (originalTripRequest == null) {
            Log.e("TrainSearchResult", "TripRequest is null");
            finish();
            return;
        }

        // Cập nhật giao diện ban đầu
        String departureStation = originalTripRequest.getDepartureStation();
        String arrivalStation = originalTripRequest.getArrivalStation();
        tvRoute.setText(departureStation + " - " + arrivalStation);

        // Lấy ngày chuyến đi từ tripRequest
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                currentTripDate = LocalDate.parse(originalTripRequest.getTripDate(), DateTimeFormatter.ISO_LOCAL_DATE);
                updateDateViews();
            } catch (Exception e) {
                Log.e("TrainSearchResult", "Invalid date format: " + originalTripRequest.getTripDate());
                finish();
                return;
            }
        }

        // Khởi tạo TrainAdapter
        adapter = new TrainAdapter(trips, trip -> {
            Intent intent = new Intent(TrainSearchResultActivity.this, TrainDetailActivity.class);
            intent.putExtra("tripId", trip.getTripId()); // tripId là int
            intent.putExtra("departureStation", trip.getDepartureStation());
            intent.putExtra("arrivalStation", trip.getArrivalStation());
            startActivity(intent);
        });

        // Cấu hình RecyclerView
        rvTrainResults.setLayoutManager(new LinearLayoutManager(this));
        rvTrainResults.setAdapter(adapter);

        // Gọi API để lấy danh sách chuyến tàu ban đầu
        fetchTrips(originalTripRequest);

        ImageButton btnPrevDate = findViewById(R.id.btnPrevDate);
        TextView tvPreviousDate = findViewById(R.id.tvPreviousDate);
        ImageButton btnNextDate = findViewById(R.id.btnNextDate);
        TextView tvNextDate = findViewById(R.id.tvNextDate);

        btnPrevDate.setOnClickListener(v -> {
            changeDate(-1, originalTripRequest);
            updatePrevButtonState(originalTripRequest.getTripDate());
        });
        tvPreviousDate.setOnClickListener(v -> {
            changeDate(-1, originalTripRequest);
            updatePrevButtonState(originalTripRequest.getTripDate());
        });

        btnNextDate.setOnClickListener(v -> {
            changeDate(1, originalTripRequest);
            updatePrevButtonState(originalTripRequest.getTripDate());
        });
        tvNextDate.setOnClickListener(v -> {
            changeDate(1, originalTripRequest);
            updatePrevButtonState(originalTripRequest.getTripDate());
        });

// Lúc khởi tạo cũng gọi updatePrevButtonState
        updatePrevButtonState(originalTripRequest.getTripDate());

//        ImageButton btnPrevDate = findViewById(R.id.btnPrevDate);
//        TextView tvPreviousDate = findViewById(R.id.tvPreviousDate);
//        ImageButton btnNextDate = findViewById(R.id.btnNextDate);
//        TextView tvNextDate = findViewById(R.id.tvNextDate);
//
//// Lấy ngày hôm nay
//        String today = null; // yyyy-MM-dd
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            today = LocalDate.now().toString();
//        }
//        String tripDate = originalTripRequest.getTripDate(); // yyyy-MM-dd
//
//// So sánh
//        if (tripDate.equals(today)) {
//            // Nếu ngày hôm nay thì disable nút "Previous"
//            btnPrevDate.setEnabled(false);
//            tvPreviousDate.setEnabled(false);
//        } else {
//            // Nếu không thì setOnClickListener như bình thường
//            btnPrevDate.setOnClickListener(v -> changeDate(-1, originalTripRequest));
//            tvPreviousDate.setOnClickListener(v -> changeDate(-1, originalTripRequest));
//        }
//
//// Các nút NextDate thì luôn setOnClickListener bình thường
//        btnNextDate.setOnClickListener(v -> changeDate(1, originalTripRequest));
//        tvNextDate.setOnClickListener(v -> changeDate(1, originalTripRequest));


        // Xử lý các nút điều hướng
//        findViewById(R.id.btnPrevDate).setOnClickListener(v -> changeDate(-1,originalTripRequest));
//        findViewById(R.id.tvPreviousDate).setOnClickListener(v -> changeDate(-1, originalTripRequest));
//        findViewById(R.id.btnNextDate).setOnClickListener(v -> changeDate(1, originalTripRequest));
//        findViewById(R.id.tvNextDate).setOnClickListener(v -> changeDate(1, originalTripRequest));

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void changeDate(int offsetDays, TripRequest tripRequest) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Cập nhật ngày mới
            currentTripDate = currentTripDate.plusDays(offsetDays);
            updateDateViews();

            // Cập nhật lại TripRequest hiện tại
            tripRequest.setTripDate(currentTripDate.format(DateTimeFormatter.ISO_LOCAL_DATE));

            // Xóa danh sách cũ và lấy danh sách chuyến tàu mới
            trips.clear();
            adapter.notifyDataSetChanged();
            fetchTrips(tripRequest);
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

    private void updatePrevButtonState(String tripDateString) {
        ImageButton btnPrevDate = findViewById(R.id.btnPrevDate);
        TextView tvPreviousDate = findViewById(R.id.tvPreviousDate);

        LocalDate today = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            today = LocalDate.now();
        }

        // Parse từ String thành LocalDate
        LocalDate tripDate = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tripDate = LocalDate.parse(tripDateString); // tự động hiểu định dạng yyyy-MM-dd
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu lỗi parse, cứ cho phép bấm nút bình thường
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tripDate = today.minusDays(1);
            }
        }

        if (tripDate.equals(today)) {
            // Nếu đang ở hôm nay thì disable và làm mờ
            btnPrevDate.setEnabled(false);
            tvPreviousDate.setEnabled(false);
            btnPrevDate.setAlpha(0.5f); // 50% mờ
            tvPreviousDate.setAlpha(0.5f);
        } else {
            // Ngày khác thì enable và sáng lên
            btnPrevDate.setEnabled(true);
            tvPreviousDate.setEnabled(true);
            btnPrevDate.setAlpha(1.0f); // 100% sáng
            tvPreviousDate.setAlpha(1.0f);
        }
    }



    private void fetchTripss(TripRequest tripRequest) {
        ApiService apiService = HTTPService.getInstance().create(ApiService.class);
        Call<List<Trip>> call = apiService.searchTripsByStationsAndDate(tripRequest);

        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    trips.clear();
                    trips.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Log.d("TrainSearchResult", "Fetched " + trips.size() + " trips");
                } else {
                    Log.e("API ER", "error: " + tripRequest.getDepartureStation()
                            + " " + tripRequest.getArrivalStation()+ " "+
                            tripRequest.getTripDate());
                    Log.e("API", "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Trip>> call, Throwable t) {
                Log.e("API", "Network error: " + t.getMessage());
            }
        });
    }

    private void fetchTrips(TripRequest tripRequest) {
        ApiService apiService = HTTPService.getInstance().create(ApiService.class);
        Call<List<Trip>> call = apiService.searchTripsByStationsAndDate(tripRequest);

        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Có chuyến tàu, hiển thị RecyclerView
                    trips.clear();
                    trips.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    rvTrainResults.setVisibility(View.VISIBLE); // Hiển thị RecyclerView
                    txtNoTrips.setVisibility(View.GONE); // Ẩn TextView "Không có chuyến tàu nào"
                    Log.d("TrainSearchResult", "Fetched " + trips.size() + " trips");
                } else {

                    // Không có chuyến tàu, hiển thị thông báo "Không có chuyến tàu nào"
                    Log.e("API ER",  "Không có chuyến tàu: " + tripRequest.getDepartureStation()
                            + " " + tripRequest.getArrivalStation() + " " + tripRequest.getTripDate());
                    rvTrainResults.setVisibility(View.GONE); // Ẩn RecyclerView
                    txtNoTrips.setVisibility(View.VISIBLE); // Hiển thị TextView "Không có chuyến tàu nào"
                }
            }

            @Override
            public void onFailure(Call<List<Trip>> call, Throwable t) {
                Log.e("API", "Network error: " + t.getMessage());
            }
        });
    }





}









//package com.example.train_app.activities;
//
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.train_app.R;
//import com.example.train_app.adapters.recyclerView.TrainAdapter;
//import com.example.train_app.api.ApiService;
//import com.example.train_app.api.HTTPService;
//import com.example.train_app.container.request.TripRequest;
//import com.example.train_app.model.Trip;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class TrainSearchResultActivity extends AppCompatActivity {
//    private RecyclerView rvTrainResults;
//    private TextView tvRoute, tvDate;
//    private List<Trip> trips = new ArrayList<>();
//    private TrainAdapter adapter;
//    private LocalDate currentTripDate;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_train_search_result);
//
//        // Khởi tạo views
//        rvTrainResults = findViewById(R.id.rvTrainResults);
//        tvRoute = findViewById(R.id.tvRoute);
//        tvDate = findViewById(R.id.tvDate);
//
//        // Lấy TripRequest từ Intent
//        TripRequest tripRequest = (TripRequest) getIntent().getSerializableExtra("tripRequest");
//        if (tripRequest == null) {
//            Log.e("TrainSearchResult", "TripRequest is null");
//            finish();
//            return;
//        }
//
//        // Cập nhật giao diện ban đầu
//        String departureStation = tripRequest.getDepartureStation();
//        String arrivalStation = tripRequest.getArrivalStation();
//        tvRoute.setText(departureStation + " - " + arrivalStation);
//
//        // Lấy ngày chuyến đi từ tripRequest
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            try {
//                currentTripDate = LocalDate.parse(tripRequest.getTripDate(), DateTimeFormatter.ISO_LOCAL_DATE);
//                updateDateViews();
//            } catch (Exception e) {
//                Log.e("TrainSearchResult", "Invalid date format: " + tripRequest.getTripDate());
//                finish();
//                return;
//            }
//        }
//
//        // Khởi tạo TrainAdapter
//        adapter = new TrainAdapter(trips, trip -> {
//            Intent intent = new Intent(TrainSearchResultActivity.this, TrainDetailActivity.class);
//            intent.putExtra("tripId", trip.getTripId()); // tripId là int
//            intent.putExtra("departureStation", trip.getDepartureStation());
//            intent.putExtra("arrivalStation", trip.getArrivalStation());
//            startActivity(intent);
//        });
//
//        // Cấu hình RecyclerView
//        rvTrainResults.setLayoutManager(new LinearLayoutManager(this));
//        rvTrainResults.setAdapter(adapter);
//
//        // Gọi API để lấy danh sách chuyến tàu
//        fetchTrips(tripRequest);
//
//        // Xử lý các nút điều hướng
//        findViewById(R.id.btnPrevDate).setOnClickListener(v -> changeDate(-1, tripRequest));
//        findViewById(R.id.tvPreviousDate).setOnClickListener(v -> changeDate(-1, tripRequest));
//        findViewById(R.id.btnNextDate).setOnClickListener(v -> changeDate(1, tripRequest));
//        findViewById(R.id.tvNextDate).setOnClickListener(v -> changeDate(1, tripRequest));
//        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
//    }
//
//    private void changeDate(int offsetDays, TripRequest tripRequest) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            currentTripDate = currentTripDate.plusDays(offsetDays);
//            updateDateViews();
//
//            // Cập nhật tripDate trong tripRequest
//            tripRequest.setTripDate(currentTripDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
//
//            // Lấy lại danh sách chuyến tàu
//            trips.clear();
//            adapter.notifyDataSetChanged();
//            fetchTrips(tripRequest);
//        }
//    }
//
//    private void updateDateViews() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//            TextView tvPrev = findViewById(R.id.tvPreviousDate);
//            TextView tvNext = findViewById(R.id.tvNextDate);
//
//            tvDate.setText(currentTripDate.format(formatter));
//            tvPrev.setText(currentTripDate.minusDays(1).format(formatter));
//            tvNext.setText(currentTripDate.plusDays(1).format(formatter));
//        }
//    }
//
//    private void fetchTrips(TripRequest tripRequest) {
//        ApiService apiService = HTTPService.getInstance().create(ApiService.class);
//        Call<List<Trip>> call = apiService.searchTripsByStationsAndDate(tripRequest);
//
//        call.enqueue(new Callback<List<Trip>>() {
//            @Override
//            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    trips.clear();
//                    trips.addAll(response.body());
//                    adapter.notifyDataSetChanged();
//                    Log.d("TrainSearchResult", "Fetched " + trips.size() + " trips");
//                } else {
//                    Log.e("API", "Response error: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Trip>> call, Throwable t) {
//                Log.e("API", "Network error: " + t.getMessage());
//            }
//        });
//    }
//}


// -------------------------------------------------------------------------------------
//package com.example.train_app.activities;
//
//
//import static com.example.train_app.helper.Format.formatDateToDashDMY;
//
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.train_app.R;
//import com.example.train_app.adapters.recyclerView.TrainAdapter;
//import com.example.train_app.api.ApiService;
//import com.example.train_app.api.HTTPService;
//import com.example.train_app.container.request.TripRequest;
//import com.example.train_app.model.Station;
//import com.example.train_app.model.Trip;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class TrainSearchResultActivity extends AppCompatActivity {
//    private RecyclerView rvTrainResults;
//    private TextView tvRoute, tvDate;
//    private List<Trip> trips = new ArrayList<>();
//    private TrainAdapter adapter;
//
//    private LocalDate currentTripDate;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_train_search_result);
//
//        rvTrainResults = findViewById(R.id.rvTrainResults);
//        tvRoute = findViewById(R.id.tvRoute);
//        tvDate = findViewById(R.id.tvDate);
//
//        TripRequest tripRequest = (TripRequest) getIntent().getSerializableExtra("tripRequest");
//        fetchTrips(tripRequest);
//        if(tripRequest == null){
//
//        }
//        String departureStation = tripRequest.getDepartureStation();
//        String arrivalStation = tripRequest.getArrivalStation();
//
//        // Lấy tham số từ Intent
////        String departureStation = getIntent().getStringExtra("departureStation") != null ? getIntent().getStringExtra("departureStation") : "Hà Nội";
////        String arrivalStation = getIntent().getStringExtra("arrivalStation") != null ? getIntent().getStringExtra("arrivalStation") : "Sài Gòn";
////        LocalDate tripDate = null;
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            tripDate = LocalDate.parse(getIntent().getStringExtra("tripDate") != null ? getIntent().getStringExtra("tripDate") : "2025-05-08");
////        }
////        tvRoute.setText(departureStation + " - " + arrivalStation);
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            tvDate.setText(tripDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
////        }
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            currentTripDate = LocalDate.parse(formatDateToDashDMY(tripRequest.getTripDate()));
//        }
//
//
//        tvRoute.setText(departureStation + " - " + arrivalStation);
//        updateDateViews(); // Cập nhật giao diện ngày
//
//
//
//       // updateDateViews(); // Cập nhật giao diện ngày
//
//        // Lấy danh sách chuyến tàu giả lập
////        fetchTrips(tripRequest);
//
//
//        adapter = new TrainAdapter(trips, trip -> {
//            Intent intent = new Intent(TrainSearchResultActivity.this, TrainDetailActivity.class);
//            intent.putExtra("tripId", trip.getTripId());
//            intent.putExtra("departureStation", trip.getDepartureStation());
//            intent.putExtra("arrivalStation", trip.getArrivalStation());
//            startActivity(intent);
//        });
//        rvTrainResults.setLayoutManager(new LinearLayoutManager(this));
//        rvTrainResults.setAdapter(adapter);
//
//        // Xử lý nút điều hướng
////        findViewById(R.id.btnPrevDate).setOnClickListener(v -> updateDate(-1));
////        findViewById(R.id.btnNextDate).setOnClickListener(v -> updateDate(1));
////        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
//        findViewById(R.id.btnPrevDate).setOnClickListener(v -> changeDate(-1, tripRequest));
//        findViewById(R.id.tvPreviousDate).setOnClickListener(v -> changeDate(-1, tripRequest));
//        findViewById(R.id.btnNextDate).setOnClickListener(v -> changeDate(1, tripRequest));
//        findViewById(R.id.tvNextDate).setOnClickListener(v -> changeDate(1, tripRequest));
//
//        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
//    }
//
//    private void updateDate(int days, TripRequest tripRequest) {
//        LocalDate currentDate = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            currentDate = LocalDate.parse(tvDate.getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//        }
//        LocalDate newDate = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            newDate = currentDate.plusDays(days);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            tvDate.setText(newDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//        }
//        trips.clear();
//        fetchTrips(tripRequest);
//        adapter.notifyDataSetChanged();
//    }
//
//    private void changeDate(int offsetDays, TripRequest tripRequest) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            currentTripDate = currentTripDate.plusDays(offsetDays);
//            updateDateViews();
//
//            trips.clear();
//            fetchTrips(tripRequest);
//            adapter.notifyDataSetChanged();
//        }
//    }
//
//    private void updateDateViews() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//            TextView tvPrev = findViewById(R.id.tvPreviousDate);
//            TextView tvNext = findViewById(R.id.tvNextDate);
//
//            tvDate.setText(currentTripDate.format(formatter));
//            tvPrev.setText(currentTripDate.minusDays(1).format(formatter));
//            tvNext.setText(currentTripDate.plusDays(1).format(formatter));
//        }
//    }
//
//
//
//    private void fetchTrips(TripRequest tripRequest) {
//        ApiService apiService = HTTPService.getInstance().create(ApiService.class);
//        Call<List<Trip>> call = apiService.searchTripsByStationsAndDate(tripRequest);
//
//        call.enqueue(new Callback<List<Trip>>() {
//            @Override
//            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    trips.clear();
//                    trips.addAll(response.body());
////                    List<Trip> tripList = response.body();
////                    if (tripList != null) {
////                        // ✅ Thành công: lấy dữ liệu tripList
////                        for (Trip trip : tripList) {
////                            Log.d("Trip", "Train: " + trip.getTrainName() + ", Date: " + trip.getTripDate());
////                        }
////
////                        // TODO: Có thể gửi tripList qua màn hình mới (TripResultActivity) hoặc hiển thị ra RecyclerView
////                    }
//                } else {
//                    // ⚠️ Xử lý lỗi API
//                    Log.e("API", "Response error: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Trip>> call, Throwable t) {
//                // ❌ Lỗi kết nối hoặc lỗi server
//                Log.e("API", "Network error: " + t.getMessage());
//            }
//        });
//    }
//}