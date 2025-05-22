package com.example.train_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.train_app.R;
import com.example.train_app.adapters.StationAdapter;
import com.example.train_app.api.ApiService;
import com.example.train_app.api.HTTPService;
import com.example.train_app.model.Station;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectStationActivity extends BaseActivity {

    private ImageButton btnBack;
    private EditText etSearchStation;
    private TextView tvTitle;
    private ListView lvStations;
    private List<Station> stationList = new ArrayList<>();
    private List<Station> filteredStationList = new ArrayList<>();
    private StationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_station);

        // Khởi tạo các view
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvTitle);
        etSearchStation = findViewById(R.id.etSearchStation);
        lvStations = findViewById(R.id.lvStations);

        // Cập nhật tiêu đề dựa trên stationType
        Intent intent = getIntent();
        String stationType = intent.getStringExtra("stationType");
        if ("departure".equals(stationType)) {
            tvTitle.setText("Chọn ga đi");
        } else if ("arrival".equals(stationType)) {
            tvTitle.setText("Chọn ga đến");
        } else {
            tvTitle.setText("Chọn ga");
        }

        // Khởi tạo adapter với danh sách đã lọc
        filteredStationList.addAll(stationList);
        adapter = new StationAdapter(this, android.R.layout.simple_list_item_1, filteredStationList);
        lvStations.setAdapter(adapter);

        // Xử lý nút Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Xử lý tìm kiếm ga
        etSearchStation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterStations(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Xử lý khi chọn một ga
        lvStations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Station selectedStation = filteredStationList.get(position);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedStation", selectedStation.getStationName());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        // Gọi API để lấy danh sách ga
        fetchStations();
    }

    private void fetchStations() {
        ApiService apiService = HTTPService.getInstance().create(ApiService.class);
        Call<List<Station>> call = apiService.getAllStations();
        call.enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    stationList.clear();
                    stationList.addAll(response.body());
                    // Cập nhật danh sách đã lọc
                    filteredStationList.clear();
                    filteredStationList.addAll(stationList);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SelectStationActivity.this, "Lỗi khi lấy danh sách ga: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                Toast.makeText(SelectStationActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String removeVietnameseTones(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        str = str.replaceAll("đ", "d").replaceAll("Đ", "D");
        return str;
    }

    private void filterStations(String query) {
        filteredStationList.clear();
        String normalizedQuery = removeVietnameseTones(query.toLowerCase());

        if (normalizedQuery.isEmpty()) {
            filteredStationList.addAll(stationList);
        } else {
            for (Station station : stationList) {
                String stationName = removeVietnameseTones(station.getStationName().toLowerCase());
                String location = removeVietnameseTones(station.getLocation().toLowerCase());

                if (stationName.contains(normalizedQuery) || location.contains(normalizedQuery)) {
                    filteredStationList.add(station);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

//    private void filterStations(String query) {
//        filteredStationList.clear();
//        if (query.isEmpty()) {
//            filteredStationList.addAll(stationList);
//        } else {
//            for (Station station : stationList) {
//                // Lọc theo stationName hoặc location, không phân biệt hoa thường
//                if (station.getStationName().toLowerCase().contains(query.toLowerCase()) ||
//                        station.getLocation().toLowerCase().contains(query.toLowerCase())) {
//                    filteredStationList.add(station);
//                }
//            }
//        }
//        adapter.notifyDataSetChanged();
//    }
}