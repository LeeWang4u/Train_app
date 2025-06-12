package com.example.train_app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.train_app.R;
import com.example.train_app.activities.SeatDetailActivity;
import com.example.train_app.activities.SelectSeatActivity;
import com.example.train_app.api.ApiService;
import com.example.train_app.api.HTTPService;
import com.example.train_app.dto.request.SelectSeatReqDTO;
import com.example.train_app.dto.request.TicketRequestDTO;
import com.example.train_app.dto.request.TicketReservationReqDTO;
import com.example.train_app.dto.response.BookingResponse;
import com.example.train_app.dto.response.TicketType;
import com.example.train_app.utils.CurrentTrip;
import com.example.train_app.utils.Format;
import com.example.train_app.utils.ReservationSeat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketInfoFragment extends BaseFragment {

    private Spinner spinnerTicketType;
    private List<TicketType> ticketTypes;
    private ProgressBar progressBar;  // Thêm ProgressBar để hiển thị khi đang tải dữ liệu

    private TextView textSeat;

    private TextView textDeparture , textDepartureTime;

    private EditText inputFullName, inputCCCD;

    private TextView textPrice;

    private TicketType selectedType;

    private BigDecimal finalPrice;

    private Button btnDelete;


    public interface OnTicketPriceChangeListener {
        void onPriceChanged();
    }
    private OnTicketPriceChangeListener priceChangeListener;
    public void setPriceChangeListener(OnTicketPriceChangeListener listener) {
        priceChangeListener = listener;
    }
    public TicketInfoFragment() {
        // Required empty public constructor
    }

    public static TicketInfoFragment newInstance() {
        return new TicketInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ticket_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

            super.onViewCreated(view, savedInstanceState);
        spinnerTicketType = view.findViewById(R.id.spinner_ticket_type);
        progressBar = view.findViewById(R.id.progress_bar);
        inputFullName = view.findViewById(R.id.input_full_name_pass);
        inputCCCD = view.findViewById(R.id.input_CCCD_pass);
        textSeat = view.findViewById(R.id.text_seat);
        textDeparture = view.findViewById(R.id.text_departure);
        textPrice = view.findViewById(R.id.text_ticket_price);
        textDepartureTime = view.findViewById((R.id.text_time));
        btnDelete = view.findViewById(R.id.btn_cancel_ticket);
        SelectSeatReqDTO seat = (SelectSeatReqDTO) getArguments().getSerializable("seat_data");
            if (seat != null) {
                textSeat.setText(Format.formatCompartment(seat.getStt(),seat.getSeatName()));
                textDeparture.setText(seat.getTicketResponseDTO().getDepartureStation()+" - "+ seat.getTicketResponseDTO().getArrivalStation());
                textPrice.setText(Format.formatPriceToVnd(seat.getTicketPrice()));
                textDepartureTime.setText(CurrentTrip.getCurrentTrip().getDepartureTime());
            }
        // Hiển thị ProgressBar khi đang tải dữ liệu
        progressBar.setVisibility(View.VISIBLE);

        // Tạo Retrofit client và gọi API
        ApiService apiService = HTTPService.getInstance().create(ApiService.class);
        Call<List<TicketType>> call = apiService.getTicketTypes();

        call.enqueue(new Callback<List<TicketType>>() {
            @Override
            public void onResponse(@NonNull Call<List<TicketType>> call, @NonNull Response<List<TicketType>> response) {
                // Ẩn ProgressBar khi đã tải xong
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    ticketTypes = response.body();

                    // Tạo danh sách tên loại vé
                    List<String> ticketTypeNames = new ArrayList<>();
                    for (TicketType ticketType : ticketTypes) {
                        ticketTypeNames.add(ticketType.getTicketTypeName());  // Thêm tên loại vé vào danh sách
                    }

                    // Tạo adapter để hiển thị tên loại vé trong Spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            ticketTypeNames
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTicketType.setAdapter(adapter);

                    // Set listener khi người dùng chọn một loại vé
                    spinnerTicketType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedType = ticketTypes.get(position);
                            finalPrice = getPrice(seat.getTicketPrice(),selectedType.getDiscountRate());
                            ReservationSeat.setFinalTotalPrice(seat.getTicketPrice(),finalPrice);
                            textPrice.setText(Format.formatPriceToVnd(finalPrice));
                            if (priceChangeListener != null) {
                                priceChangeListener.onPriceChanged();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Không làm gì khi không chọn gì
                        }
                    });
                } else {
                    Toast.makeText(requireContext(), "Không lấy được loại vé", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<TicketType>> call, @NonNull Throwable t) {
                // Ẩn ProgressBar khi gặp lỗi
                progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v->{
            ReservationSeat.removeSeat(seat);
            TicketReservationReqDTO ticketReservationReqDTO = new TicketReservationReqDTO(seat.getSeatId(), CurrentTrip.getCurrentTrip().getDepartureStation(),CurrentTrip.getCurrentTrip().getArrivalStation(),CurrentTrip.getCurrentTrip().getTripId());

            Call<BookingResponse> callDelete = apiService.deleteReserve(ticketReservationReqDTO);
            callDelete.enqueue(new Callback<BookingResponse>() {
                @Override
                public void onResponse(Call<BookingResponse> callDelete, Response<BookingResponse> response) {
                    if (response.isSuccessful()) {

                        BookingResponse bookingResponse = response.body();
                        Log.d("Reserve", "Hủy giữ chỗ thành công!" + bookingResponse.getStatus());


                    } else {
                        Log.e("Reserve", "Lỗi hủy giữ chỗ : " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<BookingResponse> callDelete, Throwable t) {
                    Log.e("Reserve", "Lỗi kết nối API", t);
                }
            });
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .remove(TicketInfoFragment.this)
                    .commit();

        });
    }

    public TicketRequestDTO getTicketInfo(){
        SelectSeatReqDTO seat = (SelectSeatReqDTO) getArguments().getSerializable("seat_data");
        TicketRequestDTO ticket = new TicketRequestDTO(seat.getTicketResponseDTO().getTicketId(),selectedType,"Hà Nội",
                "Sài Gòn",19,seat.getSeatId(),
                finalPrice,seat.getTicketPrice(),
                 inputCCCD.getText().toString().trim(),
                inputFullName.getText().toString().trim(),
                selectedType.getDiscountRate());
        return ticket;

    }
    public BigDecimal getPrice(BigDecimal a, BigDecimal b) {
        return  a.multiply(BigDecimal.valueOf(100).subtract(b))
                .divide(BigDecimal.valueOf(100));
    }
    public BigDecimal getFinalPrice(){
        return finalPrice;
    }
}
