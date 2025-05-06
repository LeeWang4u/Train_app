package com.example.train_app.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.train_app.R;
import com.example.train_app.activities.SelectSeatActivity;
import com.example.train_app.api.ApiService;
import com.example.train_app.api.HTTPService;
import com.example.train_app.dto.request.CustomerDTO;
import com.example.train_app.dto.request.SelectSeatReqDTO;
import com.example.train_app.dto.request.TicketReservationReqDTO;
import com.example.train_app.dto.response.BookingResponse;
import com.example.train_app.dto.response.CarriageAvailabilityResponseDTO;
import com.example.train_app.dto.response.SeatAvailabilityResponseDTO;
import com.example.train_app.dto.response.TicketResponseDTO;
import com.example.train_app.utils.CurrentTrip;
import com.example.train_app.utils.Format;
import com.example.train_app.utils.ReservationSeat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Coach4BedsFragment extends Fragment {
    private LinearLayout layoutCabins;
    private LayoutInflater inflater;
    public int iii = 0;

    private CarriageAvailabilityResponseDTO coach;

    private List<SeatAvailabilityResponseDTO> seats;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            coach = (CarriageAvailabilityResponseDTO) getArguments().getSerializable("coach_data");
            seats = coach.getSeats();
        }
        View rootView = inflater.inflate(R.layout.fragment_coach_4_beds, container, false);
        this.inflater = inflater;
        TextView coachName = rootView.findViewById(R.id.coach_name);
        coachName.setText(Format.formatCompartment(coach.getStt(), coach.getCompartmentName()));
        layoutCabins = rootView.findViewById(R.id.layout_cabins);

        int totalSeats =20;
        int cabins = totalSeats / 4;

        for (int cabin = 1; cabin <= cabins; cabin++) {
            LinearLayout cabinLayout = new LinearLayout(getContext());
            cabinLayout.setOrientation(LinearLayout.VERTICAL);
            cabinLayout.setPadding(0, 0, 0, 32);
            TextView cabinTitle = new TextView(getContext());
            cabinTitle.setText("Cabin " + cabin);
            cabinTitle.setTextSize(16);
            cabinTitle.setGravity(Gravity.CENTER);
            cabinTitle.setPadding(0, 16, 0, 0);
            cabinLayout.addView(cabinTitle);
            Log.d("seat", seats.get(1).getSeatNumber());
            layoutCabins.addView(cabinLayout);
            // Tầng 3 -> 1 (trên xuống)
            for (int floor = 2; floor >= 1; floor--) {
                LinearLayout row = new LinearLayout(getContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setGravity(Gravity.START);
                row.setPadding(0, 8, 0, 8);


                row.addView(createSeatView(seats.get(iii)));
                iii+=2;
                addSpacer(row);
                row.addView(createSeatView(seats.get(iii)));
                iii--;

                // Ghi tầng
                TextView floorText = new TextView(getContext());
                floorText.setText("  Floor " + floor);
                floorText.setTextColor(Color.GRAY);
                floorText.setTextSize(14);
                floorText.setPadding(64, 32, 8, 0);
                row.addView(floorText);

                cabinLayout.addView(row);
            }
            iii+=2;
            // Thêm label "Cabin X"

        }

        return rootView;
    }

    private void addSpacer(LinearLayout row) {
        View spacer = new View(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(132, 1);
        spacer.setLayoutParams(params);
        row.addView(spacer);
    }

    private View createSeatView(SeatAvailabilityResponseDTO seat) {
        View seatView = inflater.inflate(R.layout.item_bed, null);

        TextView txtNumber = seatView.findViewById(R.id.bed_number);
        TextView txtPrice = seatView.findViewById(R.id.bed_price);
        View indicator = seatView.findViewById(R.id.status_indicator);

        txtNumber.setText(String.valueOf(seat.getSeatNumber()));
        txtPrice.setText(Format.formatSeatPrice(seat.getTicketPrice()));
        SelectSeatReqDTO selectSeatReqDTO = new SelectSeatReqDTO(seat.getSeatId(),seat.getTicketPrice(),coach.getStt(), seat.getSeatNumber(),coach.getCompartmentName());

        if(ReservationSeat.checkSelectedSeat(selectSeatReqDTO)){
            indicator.setBackgroundResource(R.drawable.bg_seat_selected);
            setSeatSelectedClick(seatView, indicator,selectSeatReqDTO);
        } else{
            if (seat.isAvailable() ) {
                indicator.setBackgroundResource(R.drawable.bg_seat_available);
                setSeatAvailableClick(seatView, indicator, selectSeatReqDTO);

            } else {

                indicator.setBackgroundResource(R.drawable.bg_seat_unavailable);
                seatView.setEnabled(false); // Vô hiệu hóa click
            }
        }

        // Tùy chỉnh kích thước nếu cần
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        seatView.setLayoutParams(params);

        return seatView;
    }
    private void setSeatAvailableClick(View seatView, View indicator, SelectSeatReqDTO selectSeatReqDTO ) {
        seatView.setOnClickListener(v -> {
            if (ReservationSeat.sumSelectedSeat() < 5) {
                indicator.setBackgroundResource(R.drawable.bg_seat_selected);

                TicketReservationReqDTO ticketReservation = new TicketReservationReqDTO(selectSeatReqDTO.getSeatId(), "Hà Nội", "Sài Gòn", 19);
                ApiService apiService = HTTPService.getInstance().create(ApiService.class);
                Call<TicketResponseDTO> callReserve = apiService.reserveTicket(ticketReservation);
                callReserve.enqueue(new Callback<TicketResponseDTO>() {
                    @Override
                    public void onResponse(Call<TicketResponseDTO> call, Response<TicketResponseDTO> response) {
                        if (response.isSuccessful()) {
                            Log.d("TICKET", "Giữ vé thành công!");
                            TicketResponseDTO ticketResponseDTO = response.body();
                           selectSeatReqDTO.setTicketResponseDTO(ticketResponseDTO);

                            ReservationSeat.addSeat(selectSeatReqDTO);
                            setSeatSelectedClick(seatView, indicator, selectSeatReqDTO);
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

    private void setSeatSelectedClick(View seatView, View indicator, SelectSeatReqDTO selectSeatReqDTO) {
        seatView.setOnClickListener(v -> {
            indicator.setBackgroundResource(R.drawable.bg_seat_available);
            ReservationSeat.removeSeat(selectSeatReqDTO);
            TicketReservationReqDTO ticketReservationReqDTO = new TicketReservationReqDTO(selectSeatReqDTO.getSeatId(), CurrentTrip.getCurrentTrip().getDepartureStation(), CurrentTrip.getCurrentTrip().getArrivalStation(), CurrentTrip.getCurrentTrip().getTripId());
            ApiService apiService = HTTPService.getInstance().create(ApiService.class);
            Call<BookingResponse> call = apiService.deleteReserve(ticketReservationReqDTO);
            call.enqueue(new Callback<BookingResponse>() {
                @Override
                public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                    if (response.isSuccessful()) {

                        BookingResponse bookingResponse = response.body();
                        Log.d("Reserve", "Hủy giữ chỗ thành công!" + bookingResponse.getStatus());
                        setSeatAvailableClick(seatView, indicator, selectSeatReqDTO);
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