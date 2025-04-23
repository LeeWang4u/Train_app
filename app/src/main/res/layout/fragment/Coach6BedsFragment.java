package com.example.train.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.train.R;

public class Coach6BedsFragment extends Fragment {

    private LinearLayout layoutCabins;
    private LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_coach_6_beds, container, false);
        this.inflater = inflater;

        layoutCabins = rootView.findViewById(R.id.layout_cabins);

        int totalSeats =30; // bạn có thể set 30 nếu nhiều cabin hơn
        int seatNumber = 1;
        int cabins = totalSeats / 6;

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

            layoutCabins.addView(cabinLayout);
            // Tầng 3 -> 1 (trên xuống)
            for (int floor = 3; floor >= 1; floor--) {
                LinearLayout row = new LinearLayout(getContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setGravity(Gravity.START);
                row.setPadding(0, 8, 0, 8);

                // Thêm 2 ghế mỗi hàng
                row.addView(createSeatView(seatNumber++));
                addSpacer(row);
                row.addView(createSeatView(seatNumber++));

                // Ghi tầng
                TextView floorText = new TextView(getContext());
                floorText.setText("  Floor " + floor);
                floorText.setTextColor(Color.GRAY);
                floorText.setTextSize(14);
                floorText.setPadding(64, 32, 8, 0);
                row.addView(floorText);

                cabinLayout.addView(row);
            }

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

    private View createSeatView(int seatNumber) {
        View seatView = inflater.inflate(R.layout.item_bed, null);

        TextView txtNumber = seatView.findViewById(R.id.bed_number);
        TextView txtPrice = seatView.findViewById(R.id.bed_price);
        View indicator = seatView.findViewById(R.id.status_indicator);

        txtNumber.setText(String.valueOf(seatNumber));

        String price;
        if (seatNumber % 3 == 0) {
            price = "1074K";
        } else if (seatNumber % 3 == 1) {
            price = "1262K";
        } else {
            price = "1398K";
        }
        txtPrice.setText(price);


        seatView.setOnClickListener(v -> {
            boolean selected = v.isSelected();
            v.setSelected(!selected);
            indicator.setBackgroundResource(!selected ? R.drawable.bg_seat_selected : R.drawable.bg_seat_available);
        });

        // Tùy chỉnh kích thước nếu cần
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        seatView.setLayoutParams(params);

        return seatView;
    }
}
