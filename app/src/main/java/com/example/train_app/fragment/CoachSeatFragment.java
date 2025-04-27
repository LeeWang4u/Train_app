package com.example.train_app.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.train_app.R;
import com.example.train_app.activities.SelectSeatActivity;
import com.example.train_app.api.ApiService;
import com.example.train_app.api.HTTPService;
import com.example.train_app.dto.request.SelectSeatReqDTO;
import com.example.train_app.dto.request.TicketReservationReqDTO;
import com.example.train_app.dto.response.BookingResponse;
import com.example.train_app.dto.response.CarriageAvailabilityResponseDTO;
import com.example.train_app.dto.response.SeatAvailabilityResponseDTO;
import com.example.train_app.dto.response.TicketResponseDTO;
import com.example.train_app.utils.Format;
import com.example.train_app.utils.ReservationSeat;

import java.math.BigDecimal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoachSeatFragment extends Fragment {

    private GridLayout gridSeats;

    public int iii = 1;
    private CarriageAvailabilityResponseDTO coach;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            coach = (CarriageAvailabilityResponseDTO) getArguments().getSerializable("coach_data");
        }
        View rootView = inflater.inflate(R.layout.fragment_coach_seat, container, false);
        TextView coachName = rootView.findViewById(R.id.coach_name);
        coachName.setText(Format.formatCompartment(coach.getStt(), coach.getCompartmentName()));
        // Find GridLayout by ID
        gridSeats = rootView.findViewById(R.id.grid_seats);
//        Log.d("DEBUG_SEAT", "Danh sách số lượng ghế: " + coach.getSeats().get(1).isAvailable());
        // Add 40 seats dynamically
        for (SeatAvailabilityResponseDTO i : coach.getSeats()) {
            addSeat(i);
        }


        return rootView;
    }

    private void addSeat(SeatAvailabilityResponseDTO seat) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View seatView = inflater.inflate(R.layout.item_seat, gridSeats, false);
        LinearLayout background = seatView.findViewById(R.id.background);
        TextView seatLabel = seatView.findViewById(R.id.seat_label);
        TextView priceSeat = seatView.findViewById(R.id.seat_price);
        seatLabel.setText(String.valueOf(seat.getSeatNumber()));
        priceSeat.setText(Format.formatSeatPrice(seat.getTicketPrice()));
        // Gán layout params để đặt margin tùy theo vị trí
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = GridLayout.LayoutParams.WRAP_CONTENT;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;

        int column = (iii - 1) % 4; // vì bạn có 4 cột
        ++iii;
        if (column == 1) { // Cột thứ 2 (0-based index)
            params.setMargins(0, 0, 80, 0);
        } else if (column == 2) { // Cột thứ 3
            params.setMargins(80, 0, 0, 0);
        }

        seatView.setLayoutParams(params);
        SelectSeatReqDTO selectSeatReqDTO = new SelectSeatReqDTO(seat.getSeatId(),seat.getTicketPrice(),coach.getStt(),seat.getSeatNumber(),coach.getCompartmentName());
        if(ReservationSeat.checkSelectedSeat(selectSeatReqDTO)){
            background.setBackgroundResource(R.drawable.bg_seat_selected);
            setSeatSelectedClick(seatView, background, selectSeatReqDTO);
        } else{
            if (seat.isAvailable() ) {
                background.setBackgroundResource(R.drawable.bg_seat_available);
                setSeatAvailableClick(seatView, background, selectSeatReqDTO);

            } else {

                background.setBackgroundResource(R.drawable.bg_seat_unavailable);
                seatView.setEnabled(false); // Vô hiệu hóa click
            }
        }


        gridSeats.addView(seatView);
    }
    private void setSeatAvailableClick(View seatView, LinearLayout background,SelectSeatReqDTO selectSeatReqDTO) {
        seatView.setOnClickListener(v -> {
            if (ReservationSeat.sumSelectedSeat() < 5) {
                background.setBackgroundResource(R.drawable.bg_seat_selected);

                TicketReservationReqDTO ticketReservation = new TicketReservationReqDTO(selectSeatReqDTO.getSeatId(), "Hà Nội", "Sài Gòn", 19);
                ApiService apiService = HTTPService.getInstance().create(ApiService.class);
                Call<TicketResponseDTO> callReserve = apiService.reserveTicket(ticketReservation);
                callReserve.enqueue(new Callback<TicketResponseDTO>() {
                    @Override
                    public void onResponse(Call<TicketResponseDTO> call, Response<TicketResponseDTO> response) {
                        if (response.isSuccessful()) {
                            Log.d("TICKET", "Giữ vé thành công!");
                            TicketResponseDTO ticketResponseDTO= response.body();
                            selectSeatReqDTO.setTicketResponseDTO(ticketResponseDTO);
                            ReservationSeat.addSeat(selectSeatReqDTO);
                            setSeatSelectedClick(seatView, background, selectSeatReqDTO);
                            ((SelectSeatActivity) getActivity()).updateSummary();
                        } else {
                            Log.e("TICKET", "Lỗi giữ vé: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<TicketResponseDTO> call, Throwable t) {
                        Log.e("TICKET", "Lỗi kết nối API", t);
                    }
                });
            } else {
                Toast.makeText(getContext(), "Chỉ giữ tối đa 5 ghế!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSeatSelectedClick(View seatView, LinearLayout background, SelectSeatReqDTO selectSeatReqDTO) {
        seatView.setOnClickListener(v -> {
            background.setBackgroundResource(R.drawable.bg_seat_available);
            ReservationSeat.removeSeat(selectSeatReqDTO);
            TicketReservationReqDTO ticketReservationReqDTO = new TicketReservationReqDTO(selectSeatReqDTO.getSeatId(), "Hà Nội", "Sài Gòn", 19);
            ApiService apiService = HTTPService.getInstance().create(ApiService.class);
            Call<BookingResponse> call = apiService.deleteReserve(ticketReservationReqDTO);
            call.enqueue(new Callback<BookingResponse>() {
                @Override
                public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                    if (response.isSuccessful()) {

                        BookingResponse bookingResponse = response.body();
                        Log.d("Reserve", "Hủy giữ chỗ thành công!" + bookingResponse.getStatus());
                        setSeatAvailableClick(seatView, background, selectSeatReqDTO);
                        ((SelectSeatActivity) getActivity()).updateSummary();
                    } else {
                        Log.e("Reserve", "Lỗi hủy giữ chỗ : " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<BookingResponse> call, Throwable t) {
                    Log.e("Reserve", "Lỗi kết nối API", t);
                }
            });
        });
    }
}

