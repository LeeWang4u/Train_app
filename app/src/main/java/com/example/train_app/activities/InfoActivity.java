package com.example.train_app.activities;

import static android.app.ProgressDialog.show;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.train_app.R;
import com.example.train_app.api.ApiService;
import com.example.train_app.api.HTTPService;
import com.example.train_app.api.MomoRetrofit;
import com.example.train_app.container.request.TripDetailRequest;
import com.example.train_app.dto.request.CustomerDTO;
import com.example.train_app.dto.request.PaymentRequest;
import com.example.train_app.dto.request.ReservationCodeRequestDTO;
import com.example.train_app.dto.request.SelectSeatReqDTO;
import com.example.train_app.dto.request.TicketRequestDTO;
import com.example.train_app.dto.request.TicketReservationReqDTO;
import com.example.train_app.dto.response.BookingResponse;
import com.example.train_app.dto.response.PaymentResponse;
import com.example.train_app.fragment.TicketInfoFragment;
import com.example.train_app.payment.MomoPaymentHandler;
import com.example.train_app.utils.CurrentTrip;
import com.example.train_app.utils.Format;
import com.example.train_app.utils.ReservationSeat;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.momo.momo_partner.AppMoMoLib;

public class InfoActivity extends BaseActivity {
    // Views
    private LinearLayout fragmentContainer;
    private EditText inputFullName, inputIdNumber, inputPhone,inputEmail;

    private TextView textTotalPrice;
    private Button btnPaymentOnline;
    private Button btnPaymentOffline;

    private ImageButton btnBack;

    private ReservationCodeRequestDTO reservationCodeRequestDTO;
    private CustomerDTO customerDTO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Chuyển sang layout mới có <LinearLayout id="fragment_container">
        setContentView(R.layout.activity_customer_info);
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);
        // Ánh xạ view
        fragmentContainer = findViewById(R.id.fragment_container);
        inputFullName = findViewById(R.id.input_full_name);
        inputEmail  =findViewById(R.id.input_email);
        inputIdNumber = findViewById(R.id.input_CCCD);
        inputPhone = findViewById(R.id.input_phone);
        btnPaymentOnline = findViewById(R.id.button_payment_online);
        btnPaymentOffline = findViewById(R.id.button_payment_offline);
        btnBack = findViewById(R.id.btn_back);
        textTotalPrice = findViewById(R.id.text_total_price);

        if(ReservationSeat.sumSelectedSeat()<1){
//            TripDetailRequest tripDetailRequest = new TripDetailRequest(CurrentTrip.getCurrentTrip().getArrivalStation(),CurrentTrip.getCurrentTrip().getDepartureStation(), CurrentTrip.getCurrentTrip().getTripId());
//            Intent intent = new Intent(InfoActivity.this, SelectSeatActivity.class);
//            intent.putExtra("tripDetailRequest",tripDetailRequest);
//            intent.putExtra("trip",CurrentTrip.getCurrentTrip());
//            startActivity(intent);
            finish();
        }



        // Chỉ add fragment lần đầu, tránh add lại khi rotate màn hình
        if (savedInstanceState == null) {
            textTotalPrice.setText(Format.formatPriceToVnd(ReservationSeat.getFinalTotalPrice()));

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            for (SelectSeatReqDTO seat : ReservationSeat.getSelectedSeats()) {
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


                TicketInfoFragment frag = new TicketInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("seat_data", seat);
                frag.setArguments(bundle);
                frag.setPriceChangeListener(new TicketInfoFragment.OnTicketPriceChangeListener() {
                    @Override
                    public void onPriceChanged() {
                        textTotalPrice.setText(Format.formatPriceToVnd(ReservationSeat.getFinalTotalPrice()));
                    }
                });
                ft.add(viewId, frag);
            }

            ft.commit();

        }
        btnBack.setOnClickListener(v -> {
//            TripDetailRequest tripDetailRequest = new TripDetailRequest(CurrentTrip.getCurrentTrip().getArrivalStation(),CurrentTrip.getCurrentTrip().getDepartureStation(), CurrentTrip.getCurrentTrip().getTripId());
//            Intent intent = new Intent(InfoActivity.this, SelectSeatActivity.class);
//            intent.putExtra("tripDetailRequest",tripDetailRequest);
//            intent.putExtra("trip",CurrentTrip.getCurrentTrip());
//            startActivity(intent);
            finish();
        });

        // Xử lý nút Xác nhận
        btnPaymentOnline.setOnClickListener(v -> {
            new AlertDialog.Builder(InfoActivity.this)
                    .setTitle("Xác nhận thanh toán")
                    .setMessage("Bạn có chắc muốn thanh toán không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        callConfirmTicketApi();
                        handleMomo();
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });

        btnPaymentOffline.setOnClickListener(v -> {
            new AlertDialog.Builder(InfoActivity.this)
                    .setTitle("Xác nhận thanh toán")
                    .setMessage("Bạn có chắc muốn thanh toán không?" +"\n"+  "Vui lòng đến quầy trước 30 phút để thanh toán")
                    .setPositiveButton("Đồng ý", (dialog, which) -> {
                        callConfirmTicketApi();
                        handleCallConfirmTicket();
                        ReservationSeat.clearSelectedSeats();
                        ReservationSeat.clearTotalPrice();
                        Intent intent = new Intent(InfoActivity.this, SearchTrainActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });
    }
    private void callConfirmTicketApi() {
        // Giả sử bạn có mã đặt vé lấy từ input hoặc Intent
        customerDTO = new CustomerDTO();
        customerDTO.setFullName(inputFullName.getText().toString().trim());
        customerDTO.setEmail(inputEmail.getText().toString().trim());
        customerDTO.setCccd(inputIdNumber.getText().toString().trim());
        customerDTO.setPhone(inputPhone.getText().toString().trim());
        reservationCodeRequestDTO = new ReservationCodeRequestDTO();
        reservationCodeRequestDTO.setCustomerDTO(customerDTO);
        List<TicketRequestDTO> ticketRequestDTO = new ArrayList<>();
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        for (Fragment frag : fragments) {
            if (frag instanceof TicketInfoFragment) {
                TicketRequestDTO ticket = ((TicketInfoFragment) frag).getTicketInfo();
                if (ticket != null) {
                    ticketRequestDTO.add(ticket);
                }
            }
        }
        reservationCodeRequestDTO.setTicketRequestDTO(ticketRequestDTO);
    }
    private void handleMomo(){
        String customerName = customerDTO.getFullName(); // Get from user input or data
        BigDecimal amount = ReservationSeat.getFinalTotalPrice(); // Example amount in VND, replace with actual value

        PaymentRequest request = new PaymentRequest(customerName, amount);
        ApiService apiService = MomoRetrofit.getApiService();

        apiService.createPayment(request).enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String payUrl = response.body().getPayUrl();
                    Log.d("PayUrl","payurl:"+ payUrl);
                    if (payUrl != null && !payUrl.isEmpty()) {
                        handleCallConfirmTicket();
                        // Start PaymentActivity with the payment URL
                        Intent intent = new Intent(InfoActivity.this, PaymentActivity.class);
                        intent.putExtra("payment_url", payUrl);
                        startActivity(intent);
                    } else {
                        Toast.makeText(InfoActivity.this, "Failed to get payment URL", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(InfoActivity.this, "Payment request failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                Toast.makeText(InfoActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void handleCallConfirmTicket(){
        ApiService api = HTTPService
                .getInstance()
                .create(ApiService.class);
        Call<BookingResponse> call = api.confirmTicket(reservationCodeRequestDTO);

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
    //Get token through MoMo app

}
