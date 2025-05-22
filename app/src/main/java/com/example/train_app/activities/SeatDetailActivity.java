package com.example.train_app.activities;

import static androidx.core.app.PendingIntentCompat.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.train_app.MainActivity;
import com.example.train_app.R;
import com.example.train_app.api.ApiService;
import com.example.train_app.api.HTTPService;
import com.example.train_app.container.request.TripDetailRequest;
import com.example.train_app.dto.request.SelectSeatReqDTO;
import com.example.train_app.dto.request.TicketReservationReqDTO;
import com.example.train_app.dto.response.BookingResponse;
import com.example.train_app.utils.CurrentTrip;
import com.example.train_app.utils.Format;
import com.example.train_app.utils.ReservationSeat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SeatDetailActivity extends AppCompatActivity {

    private LinearLayout seatContainer;
    private TextView textViewTotalAmount , label;
    private Button buttonThanhToan;
    private ImageButton btnBack;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_seat_detail);
            seatContainer       = findViewById(R.id.seat_container);
            textViewTotalAmount = findViewById(R.id.text_view_total_amount);
            buttonThanhToan      = findViewById(R.id.button_thanh_toan);
            btnBack = findViewById(R.id.btn_back);
            label = findViewById(R.id.label);
            label.setText(Format.formatLabel(CurrentTrip.getCurrentTrip().getDepartureStation(),CurrentTrip.getCurrentTrip().getArrivalStation(),CurrentTrip.getCurrentTrip().getDepartureTime(),CurrentTrip.getCurrentTrip().getTrainName()));
            LayoutInflater inflater = getLayoutInflater();
            for(SelectSeatReqDTO seat: ReservationSeat.getSelectedSeats()){
                View item = inflater.inflate(R.layout.item_seat_detail,seatContainer,false);
                TextView tvType      = item.findViewById(R.id.textViewSeatType);
                TextView tvCoachSeat = item.findViewById(R.id.textViewCoachAndSeat);
                TextView tvPrice     = item.findViewById(R.id.textViewPrice);
                Button   btnDelete   = item.findViewById(R.id.buttonDelete);

                tvType.setText(seat.getCompartmentName());
                tvCoachSeat.setText(Format.formatCompartment(seat.getStt(),seat.getSeatName()));
                tvPrice.setText(Format.formatPriceToVnd(seat.getTicketPrice()));

                btnDelete.setOnClickListener(v->{
                    ReservationSeat.removeSeat(seat);
                    seatContainer.removeView(item);
                    TicketReservationReqDTO ticketReservationReqDTO = new TicketReservationReqDTO(seat.getSeatId(), CurrentTrip.getCurrentTrip().getDepartureStation(),CurrentTrip.getCurrentTrip().getArrivalStation(),CurrentTrip.getCurrentTrip().getTripId());
                    ApiService apiService = HTTPService.getInstance().create(ApiService.class);
                    Call<BookingResponse> call = apiService.deleteReserve(ticketReservationReqDTO);
                    call.enqueue(new Callback<BookingResponse>() {
                        @Override
                        public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                            if (response.isSuccessful()) {

                                BookingResponse bookingResponse = response.body();
                                Log.d("Reserve", "Hủy giữ chỗ thành công!" + bookingResponse.getStatus());


                            } else {
                                Log.e("Reserve", "Lỗi hủy giữ chỗ : " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<BookingResponse> call, Throwable t) {
                            Log.e("Reserve", "Lỗi kết nối API", t);
                        }
                    });
                    if(ReservationSeat.sumSelectedSeat()<1){
//                        TripDetailRequest tripDetailRequest = new TripDetailRequest(CurrentTrip.getCurrentTrip().getArrivalStation(),CurrentTrip.getCurrentTrip().getDepartureStation(), CurrentTrip.getCurrentTrip().getTripId());
//                        Intent intent = new Intent(SeatDetailActivity.this, SelectSeatActivity.class);
//                        intent.putExtra("tripDetailRequest",tripDetailRequest);
//                        intent.putExtra("trip",CurrentTrip.getCurrentTrip());
//                        startActivity(intent);
                        finish();
                    }

                });

                seatContainer.addView(item);

            }

            textViewTotalAmount.setText(Format.formatPriceToVnd(ReservationSeat.getTotalPrice()));

            buttonThanhToan.setOnClickListener(v -> {
                Intent in = new Intent(SeatDetailActivity.this, InfoActivity.class);
                startActivity(in);
            });

            btnBack.setOnClickListener(v->{
//                TripDetailRequest tripDetailRequest = new TripDetailRequest(CurrentTrip.getCurrentTrip().getArrivalStation(),CurrentTrip.getCurrentTrip().getDepartureStation(), CurrentTrip.getCurrentTrip().getTripId());
//                Intent intent = new Intent(SeatDetailActivity.this, SelectSeatActivity.class);
//                intent.putExtra("tripDetailRequest",tripDetailRequest);
//                intent.putExtra("trip",CurrentTrip.getCurrentTrip());
//                startActivity(intent);
                finish();

            });
        }


    }

