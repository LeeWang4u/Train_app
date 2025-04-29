package com.example.train_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.example.train_app.dto.request.CustomerDTO;
import com.example.train_app.dto.request.ReservationCodeRequestDTO;
import com.example.train_app.dto.request.SelectSeatReqDTO;
import com.example.train_app.dto.request.TicketRequestDTO;
import com.example.train_app.dto.request.TicketReservationReqDTO;
import com.example.train_app.dto.response.BookingResponse;
import com.example.train_app.fragment.TicketInfoFragment;
import com.example.train_app.utils.Format;
import com.example.train_app.utils.ReservationSeat;

import org.json.JSONException;
import org.json.JSONObject;

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

public class InfoActivity extends AppCompatActivity {
    // Views
    private LinearLayout fragmentContainer;
    private EditText inputFullName, inputIdNumber, inputPhone,inputEmail;

    private TextView textTotalPrice;
    private Button btnPayment,btnBack;

    private ReservationCodeRequestDTO reservationCodeRequestDTO;

    private String amount = "10000";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "HoangNgoc"; //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas

    private String merchantCode = "MOMOC2IC20220501";//Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
    private String merchantNameLabel = "Đường sắt số một Việt Nam";
    private String description = "Thanh toán đặt vé tàu online";
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
        btnPayment = findViewById(R.id.btn_payment);
        btnBack = findViewById(R.id.btn_back);
        textTotalPrice = findViewById(R.id.text_total_price);





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
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFullName(inputFullName.getText().toString().trim());
        customerDTO.setEmail(inputEmail.getText().toString().trim());
        customerDTO.setCccd(inputIdNumber.getText().toString().trim());
        customerDTO.setPhone(inputPhone.getText().toString().trim());
        reservationCodeRequestDTO = new ReservationCodeRequestDTO();
        reservationCodeRequestDTO.setCustomerDTO(customerDTO);
        List<TicketRequestDTO> ticketRequestDTO = new ArrayList<>();
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        for(Fragment frag:fragments){
            if(frag instanceof TicketInfoFragment) {
                TicketRequestDTO ticket = ((TicketInfoFragment) frag).getTicketInfo();
                if(ticket!=null){
                    ticketRequestDTO.add(ticket);
                }
            }
        }
        reservationCodeRequestDTO.setTicketRequestDTO(ticketRequestDTO);
        requestPayment(customerDTO);
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
    private void requestPayment(CustomerDTO customerDTO) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
        amount = Format.formatPriceToVnd(ReservationSeat.getFinalTotalPrice());
        String orderId = "DSVN" + System.currentTimeMillis();
        String orderLabel = customerDTO.getFullName() + System.currentTimeMillis();
        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", ReservationSeat.getFinalTotalPrice().setScale(0, RoundingMode.HALF_UP).intValueExact()); //Kiểu integer
        eventValue.put("orderId", orderId);
        eventValue.put("orderLabel", orderLabel); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ bán vé tàu");//gán nhãn
        eventValue.put("fee", 0); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId",  merchantCode+"merchant_billId_"+System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);


    }
    //Get token callback from MoMo app an submit to server side
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {
                if(data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
//                    tvMessage.setText("message: " + "Get token " + data.getStringExtra("message"));
                    String token = data.getStringExtra("data"); //Token response
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if(env == null){
                        env = "app";
                    }

                    if(token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
                    } else {
//                        tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
//                    tvMessage.setText("message: " + message);
                } else if(data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                } else {
                    //TOKEN FAIL
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                }
            } else {
//                tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
            }
        } else {
//            tvMessage.setText("message: " + this.getString(R.string.not_receive_info_err));
        }
    }
}
