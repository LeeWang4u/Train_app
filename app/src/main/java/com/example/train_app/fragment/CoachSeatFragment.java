package com.example.train_app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.train_app.R;

public class CoachSeatFragment extends Fragment {

    private GridLayout gridSeats;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_coach_seat, container, false);

        // Find GridLayout by ID
        gridSeats = rootView.findViewById(R.id.grid_seats);

        // Add 40 seats dynamically
        for (int i = 1; i <= 40; i++) {
            addSeat(i);
        }

        return rootView;
    }

    private void addSeat(int seatNumber) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View seatView = inflater.inflate(R.layout.item_seat, gridSeats, false);

        TextView seatLabel = seatView.findViewById(R.id.seat_label);
        seatLabel.setText(String.valueOf(seatNumber));

        // Gán layout params để đặt margin tùy theo vị trí
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = GridLayout.LayoutParams.WRAP_CONTENT;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;

        int column = (seatNumber - 1) % 4; // vì bạn có 4 cột
        if (column == 1) { // Cột thứ 2 (0-based index)
            params.setMargins(0, 0, 80, 0);
        } else if (column == 2) { // Cột thứ 3
            params.setMargins(80, 0, 0, 0);
        }

        seatView.setLayoutParams(params);

        seatView.setOnClickListener(v -> {
            seatLabel.setBackgroundResource(R.drawable.bg_seat_selected);
        });

        gridSeats.addView(seatView);
    }

}

