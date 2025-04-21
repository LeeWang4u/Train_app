package com.example.train_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.train_app.R;
import com.example.train_app.adapters.StationAdapter;
import com.example.train_app.api.ApiService;
import com.example.train_app.api.HTTPService;
import com.example.train_app.model.Station;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectStationActivity extends AppCompatActivity {

    private ListView lvStations;
    private List<Station> stationList = new ArrayList<>();
    private StationAdapter adapter; // Sử dụng StationAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_station);

        lvStations = findViewById(R.id.lvStations);

        // Khởi tạo StationAdapter
        adapter = new StationAdapter(this, android.R.layout.simple_list_item_1, stationList);
        lvStations.setAdapter(adapter);

        // Gọi API để lấy danh sách ga
        fetchStations();

        // Xử lý khi chọn một ga
        lvStations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Station selectedStation = stationList.get(position);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedStation", selectedStation.getStationName());
                setResult(RESULT_OK, resultIntent);
                finish();
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
                    stationList.clear();
                    stationList.addAll(response.body());
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
}