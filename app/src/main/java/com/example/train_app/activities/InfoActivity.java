package com.example.train_app.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.train_app.R;
import com.example.train_app.api.ApiService;
import com.example.train_app.api.HTTPService;
import com.example.train_app.dto.request.ReservationCodeRequestDTO;
import com.example.train_app.dto.response.BookingResponse;
import com.example.train_app.fragment.TicketInfoFragment;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoActivity extends AppCompatActivity {
    // Views
    private LinearLayout fragmentContainer;
    private EditText inputFullName, inputIdNumber, inputPhone;
    private Button btnPayment,btnBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Chuyển sang layout mới có <LinearLayout id="fragment_container">
        setContentView(R.layout.activity_customer_info);

        // Ánh xạ view
        fragmentContainer = findViewById(R.id.fragment_container);
        inputFullName = findViewById(R.id.input_full_name);
        inputIdNumber = findViewById(R.id.input_id_number);
        inputPhone = findViewById(R.id.input_phone);
        btnPayment = findViewById(R.id.btn_payment);
        btnBack = findViewById(R.id.btn_back);

        List<String> tickets = Arrays.asList("Ticket 1", "Ticket 2");

        // Chỉ add fragment lần đầu, tránh add lại khi rotate màn hình
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            for (int i = 0; i < tickets.size(); i++) {
                // 1) Tạo FrameLayout động làm container cho mỗi Fragment
                FrameLayout frame = new FrameLayout(this);
                int viewId = View.generateViewId();
                frame.setId(viewId);

                // Thiết lập layout params
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                lp.setMargins(0, 8, 0, 8);
                fragmentContainer.addView(frame, lp);

                // 2) Tạo Fragment và add vào FrameLayout vừa tạo
                TicketInfoFragment frag = TicketInfoFragment.newInstance();
                ft.add(viewId, frag);
            }

            ft.commit();
        }
        btnBack.setOnClickListener(v -> {
            finish();  // Kết thúc Activity hiện tại và quay lại màn hình trước
        });

        // Xử lý nút Xác nhận
        btnPayment.setOnClickListener(v -> {
            new AlertDialog.Builder(InfoActivity.this)
                    .setTitle("Xác nhận thanh toán")
                    .setMessage("Bạn có chắc muốn thanh toán không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        // Nếu OK → gọi API confirmTicket
                        callConfirmTicketApi();
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });
    }
    private void callConfirmTicketApi() {
        // Giả sử bạn có mã đặt vé lấy từ input hoặc Intent
        String reservationCode = "ABC123"; // Thay bằng mã thực tế

        ReservationCodeRequestDTO dto = new ReservationCodeRequestDTO();

        ApiService api = HTTPService
                .getInstance()
                .create(ApiService.class);
        Call<BookingResponse> call = api.confirmTicket(dto);

        call.enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call,
                                   Response<BookingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookingResponse body = response.body();
                    // Giả sử backend trả status = "SUCCESS" khi thanh toán OK
                    if ("SUCCESS".equalsIgnoreCase(body.getStatus())) {
                        Toast.makeText(InfoActivity.this,
                                "Thanh toán thành công!\n" + body.getMessage(),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(InfoActivity.this,
                                "Thanh toán thất bại: " + body.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(InfoActivity.this,
                            "Server lỗi: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                Toast.makeText(InfoActivity.this,
                        "Không thể kết nối: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
