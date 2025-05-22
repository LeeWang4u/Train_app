package com.example.train_app.activities;

import static java.security.AccessController.getContext;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.train_app.R;
import com.example.train_app.api.ApiService;
import com.example.train_app.api.HTTPService;
import com.example.train_app.dto.response.SearchTicketResponse;
import com.example.train_app.dto.response.TicketResponseDTO;
import com.example.train_app.dto.response.TripAvailabilityResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckTicketActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private RadioButton radioTicketCode, radioSeatCode;
    private EditText editTextCode;
    private Button btnCheck;
    private LinearLayout ticketContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_ticket); // sửa lại nếu tên layout khác

        // Ánh xạ view
        radioGroup = findViewById(R.id.radioGroup);
        radioTicketCode = findViewById(R.id.radioTicketCode);
        radioSeatCode = findViewById(R.id.radioSeatCode);
        editTextCode = findViewById(R.id.editTextCode);
        btnCheck = findViewById(R.id.btnCheck);
        ticketContainer = findViewById(R.id.ticketContainer);



        btnCheck.setOnClickListener(v -> checkTicket());
    }

    private void checkTicket() {
        String input = editTextCode.getText().toString().trim();

        if (input.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Mã là: " + input, Toast.LENGTH_SHORT).show();
        ticketContainer.removeAllViews(); // clear cũ
        ticketContainer.setVisibility(View.VISIBLE);

        try {
            int code = Integer.parseInt(input);

            if (radioTicketCode.isChecked()) {
                ApiService apiService = HTTPService.getInstance().create(ApiService.class);
                Call<SearchTicketResponse> call = apiService.searchTicketById(code);
                call.enqueue(new Callback<SearchTicketResponse>() {
                    @Override
                    public void onResponse(Call<SearchTicketResponse> call, Response<SearchTicketResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            addTicketView(response.body());
                        } else {
                            showNotFound();
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchTicketResponse> call, Throwable t) {
                        showError(t);
                    }
                });
            } else {
                ApiService apiService = HTTPService.getInstance().create(ApiService.class);
                Call<List<SearchTicketResponse>> call = apiService.searchTicketByReservationCodeAndroid(code);
                call.enqueue(new Callback<List<SearchTicketResponse>>() {
                    @Override
                    public void onResponse(Call<List<SearchTicketResponse>> call, Response<List<SearchTicketResponse>> response) {
                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                            for (SearchTicketResponse ticket : response.body()) {
                                addTicketView(ticket);
                            }
                        } else {
                            showNotFound();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SearchTicketResponse>> call, Throwable t) {
                        showError(t);
                    }
                });
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Mã không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private void addTicketView(SearchTicketResponse ticket) {
        View ticketView = LayoutInflater.from(this).inflate(R.layout.item_ticket, ticketContainer, false);
        TextView tvTicketCode = ticketView.findViewById(R.id.tvTicketCode);
        TextView tvPassengerName = ticketView.findViewById(R.id.tvPassengerName);
        TextView tvRoute = ticketView.findViewById(R.id.tvRoute);
        TextView tvTrainName = ticketView.findViewById(R.id.tvTrainName);
        TextView tvSeat = ticketView.findViewById(R.id.tvSeat);

        tvTicketCode.setText("Mã vé: " + ticket.getTicketId());
        tvPassengerName.setText("Tên hành khách: " + ticket.getPassengerName());
        tvRoute.setText("Ga đi - Ga đến: " + ticket.getDepartureStation()+" - " + ticket.getArrivalStation());
        tvTrainName.setText("Tên tàu: " + ticket.getTrainName());
        tvSeat.setText("Chỗ ghế: " + ticket.getseatName());

        ticketContainer.addView(ticketView);
        ticketContainer.setVisibility(View.VISIBLE);
    }

    private void showNotFound() {
        Toast.makeText(this, "Không tìm thấy vé", Toast.LENGTH_SHORT).show();
    }

    private void showError(Throwable t) {
        Toast.makeText(this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
