package com.example.train_app.activities;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.train_app.R;
import com.example.train_app.api.HTTPService;
import com.example.train_app.api.ApiService;
import com.example.train_app.container.request.TripDetailRequest;
import com.example.train_app.dto.request.TripSeatRequestDTO;
import com.example.train_app.dto.response.CarriageAvailabilityResponseDTO;
import com.example.train_app.dto.response.TripAvailabilityResponseDTO;
import com.example.train_app.fragment.Coach4BedsFragment;
import com.example.train_app.fragment.Coach6BedsFragment;
import com.example.train_app.fragment.CoachSeatFragment;
import com.example.train_app.model.Trip;
import com.example.train_app.utils.CurrentTrip;
import com.example.train_app.utils.Format;
import com.example.train_app.utils.ReservationSeat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectSeatActivity extends BaseActivity {

    private LinearLayout fragmentContainer;
    private ProgressBar progressBar;
    private ScrollView scrollView;
    private Button continueButton;
    private TextView progressText , label;

    private LinearLayout summaryContainer;
    private TextView tvSeatCount;
    private TextView tvTotalAmount;
    private Button btnDetail;
     private ImageButton btnBack ;

    private TripDetailRequest tripDetailRequest;

    private Trip trip;
    private List<CarriageAvailabilityResponseDTO> cachedCoachList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seat);

        // Ánh xạ view
        fragmentContainer = findViewById(R.id.fragment_container);
        progressBar = findViewById(R.id.progress_bar);
        scrollView = findViewById(R.id.scrollView);
        continueButton = findViewById(R.id.btn_continue);
        progressText = findViewById(R.id.progress_text);
        summaryContainer = findViewById(R.id.summary_container);
        tvSeatCount = findViewById(R.id.tv_seat_count);
        tvTotalAmount = findViewById(R.id.tv_total_amount);
        btnDetail = findViewById(R.id.btn_detail);
        btnBack = findViewById(R.id.btn_back);
        label = findViewById(R.id.label);


        continueButton.setVisibility(View.GONE);
//
        Intent intent = getIntent();
         tripDetailRequest = (TripDetailRequest) intent.getSerializableExtra("tripDetailRequest");
         trip = (Trip) intent.getSerializableExtra("trip");
        CurrentTrip.setTrip(trip);

        Log.d("API_SUCCESS", "Số lượng toa tàu: " + tripDetailRequest.getDepartureStation()+ " - "+ tripDetailRequest.getArrivalStation());
        TripSeatRequestDTO tripSeatRequestDTO = new TripSeatRequestDTO(tripDetailRequest.getIdTrip(),tripDetailRequest.getArrivalStation(),tripDetailRequest.getDepartureStation());
            ApiService apiService = HTTPService.getInstance().create(ApiService.class);
            Call<TripAvailabilityResponseDTO> call = apiService.getCarriagesAndSeat(tripSeatRequestDTO);
            label.setText(Format.formatLabel(trip.getDepartureStation(),trip.getArrivalStation(),trip.getDepartureTime(),trip.getTrainName()));
            call.enqueue(new Callback<TripAvailabilityResponseDTO>() {
                @Override
                public void onResponse(Call<TripAvailabilityResponseDTO> call, Response<TripAvailabilityResponseDTO> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<CarriageAvailabilityResponseDTO> coachList = response.body().getCarriages();
                        Log.d("API_SUCCESS", "Số lượng toa tàu: " + coachList.size());
                        cachedCoachList = coachList;
                        loadFragments(cachedCoachList);

                        progressBar.setVisibility(View.GONE);
                        progressText.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        continueButton.setVisibility(View.VISIBLE);

                        updateSummary();
                    } else {
                        Log.e("API_ERROR", "Mã lỗi: " + response.code());
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Không có nội dung lỗi";
                            Log.e("API_ERROR", "Nội dung lỗi: " + errorBody);
                        } catch (Exception e) {
                            Log.e("API_ERROR", "Không thể đọc errorBody");
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<TripAvailabilityResponseDTO> call, Throwable t) {
                    Log.e("API", "API call failed", t);
                }
            });


        continueButton.setOnClickListener(v -> {
            if (ReservationSeat.sumSelectedSeat() > 0) {
                Intent in = new Intent(SelectSeatActivity.this, InfoActivity.class);
                startActivity(in);
            } else {
                Toast.makeText(SelectSeatActivity.this, "Vui lòng chọn ghế trước khi thanh toán", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> {
            finish();  // Kết thúc Activity hiện tại và quay lại màn hình trước
        });

        btnDetail.setOnClickListener(v -> {
            Intent in = new Intent(SelectSeatActivity.this,SeatDetailActivity.class);
            startActivity(in);
        });
    }

    private void loadFragments(List<CarriageAvailabilityResponseDTO> coaches) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        for (CarriageAvailabilityResponseDTO coach : coaches) {
            String type = coach.getCompartmentName();  // ví dụ: "Ngồi mềm điều hòa", "Giường nằm khoang 6 điều hòa"

            FrameLayout frame = new FrameLayout(this);
            int viewId = View.generateViewId();
            frame.setId(viewId);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            lp.setMargins(0, 16, 0, 16);
            fragmentContainer.addView(frame, lp);

            Fragment frag;
            Bundle bundle = new Bundle();
            bundle.putSerializable("coach_data", coach);
            switch (type) {
                case "Ngồi mềm điều hòa":
                    frag = new CoachSeatFragment();
                    break;
                case "Giường nằm khoang 6 điều hòa":
                    frag = new Coach6BedsFragment();
                    break;
                case "Giường nằm khoang 4 điều hòa":
                    frag = new Coach4BedsFragment();
                    break;
                default:
                    frag = new CoachSeatFragment(); // fallback
                    break;
            }
            frag.setArguments(bundle);
            ft.add(viewId, frag);
        }

        ft.commit();
    }
    public void updateSummary() {
        if (ReservationSeat.sumSelectedSeat()<1) {
            summaryContainer.setVisibility(View.GONE);
        } else {
            summaryContainer.setVisibility(View.VISIBLE);

            int seatCount = ReservationSeat.sumSelectedSeat();

            tvSeatCount.setText(seatCount + " Ghế");
            tvTotalAmount.setText(Format.formatPriceToVnd(ReservationSeat.getTotalPrice()));
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (cachedCoachList != null && !cachedCoachList.isEmpty()) {
            fragmentContainer.removeAllViews();
            loadFragments(cachedCoachList);
            updateSummary();
        }
    }

}
