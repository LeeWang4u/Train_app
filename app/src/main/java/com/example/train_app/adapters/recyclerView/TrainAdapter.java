package com.example.train_app.adapters.recyclerView;

import static com.example.train_app.helper.Format.formatLocalDateTimeToHHmm;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.train_app.R;
import com.example.train_app.activities.TrainDetailActivity;
import com.example.train_app.container.request.TripDetailRequest;
import com.example.train_app.model.Trip;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TrainAdapter extends RecyclerView.Adapter<TrainAdapter.TrainViewHolder> {
    private final List<Trip> trips;
    private final OnItemClickListener onTrainClickListener;

    public interface OnItemClickListener {
        void onItemClick(Trip trip);
    }

    public TrainAdapter(List<Trip> trips, OnItemClickListener listener) {
        this.trips = trips;
        this.onTrainClickListener = listener;
    }

    @NonNull
    @Override
    public TrainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
        return new TrainViewHolder(view);
    }

//    @Override
//    public void onBindViewHolder(@NonNull TrainViewHolder holder, int position) {
//        Trip trip = trips.get(position);
//        holder.bind(trip);
//        holder.itemView.setOnClickListener(v -> onTrainClickListener.onItemClick(trip));
//
//    }

    @Override
    public void onBindViewHolder(@NonNull TrainViewHolder holder, int position) {
        Trip trip = trips.get(position);
        holder.bind(trip);

        // Khi người dùng click vào một mục, chuyển dữ liệu sang Activity mới
        holder.itemView.setOnClickListener(v -> {
            if (onTrainClickListener != null) {
                onTrainClickListener.onItemClick(trip);

                TripDetailRequest tripDetailRequest = new TripDetailRequest(trip.getDepartureStation(),
                        trip.getArrivalStation(), trip.getTripId());

                // Tạo Intent để chuyển sang màn hình mới
                Intent intent = new Intent(holder.itemView.getContext(), TrainDetailActivity.class);
                intent.putExtra("tripDetailRequest", tripDetailRequest);


                Log.d("ChiTietTau", trip.getTripId() + " " + trip.getDepartureStation() + " " + trip.getDepartureStation());

                // Khởi động Activity mới
                holder.itemView.getContext().startActivity(intent);


            }
        });
    }


    @Override
    public int getItemCount() {
        return trips.size();
    }

    public class TrainViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDepartureTime, txtDepartureStation, txtArrivalTime,
                txtArrivalStation, txtDuration, txtTrainName, txtAvailableSeats;
        private Button btnSelect;


        public TrainViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDepartureTime = itemView.findViewById(R.id.txt_departure_time);
            txtDepartureStation = itemView.findViewById(R.id.txt_departure_station);
            txtArrivalTime = itemView.findViewById(R.id.txt_arrival_time);
            txtArrivalStation = itemView.findViewById(R.id.txt_arrival_station);
            txtDuration = itemView.findViewById(R.id.txt_duration);
            txtTrainName = itemView.findViewById(R.id.txt_train_name);
            txtAvailableSeats = itemView.findViewById(R.id.txt_available_seats);
            btnSelect = itemView.findViewById(R.id.btn_select);

        }

        public void bind(Trip trip) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                txtDepartureTime.setText(formatLocalDateTimeToHHmm(trip.getDepartureTime()));
                txtArrivalTime.setText(formatLocalDateTimeToHHmm(trip.getArrivalTime()));
                txtDuration.setText(calculateDuration(trip.getDepartureTime(), trip.getArrivalTime()));
            } else {
                txtDepartureTime.setText("N/A");
                txtArrivalTime.setText("N/A");
                txtDuration.setText("N/A");
            }

            txtDepartureStation.setText(trip.getDepartureStation() != null ?
                    trip.getDepartureStation() : "N/A");
            txtArrivalStation.setText(trip.getArrivalStation() != null ?
                    trip.getArrivalStation() : "N/A");
            txtTrainName.setText(trip.getTrainName() != null ?
                    trip.getTrainName() : "N/A");
            txtAvailableSeats.setText(trip.getAvailableSeats() >= 0 ?
                    "Còn " + trip.getAvailableSeats() + " chỗ" : "N/A");

            btnSelect.setOnClickListener(v -> {
                if (onTrainClickListener != null) {
                    onTrainClickListener.onItemClick(trip);
                }
            });
        }

        private String calculateDuration(String startTimeString, String endTimeString) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O ||
                    startTimeString == null || startTimeString.isEmpty() ||
                    endTimeString == null || endTimeString.isEmpty()) {
                return "N/A";
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime start = LocalDateTime.parse(startTimeString, formatter);
                LocalDateTime end = LocalDateTime.parse(endTimeString, formatter);

                Duration duration = Duration.between(start, end);
                if (duration.isNegative()) {
                    return "Thời gian không hợp lệ";
                }

                long days = duration.toDays();
                long hours = duration.toHours() % 24;
                long minutes = duration.toMinutes() % 60;

                StringBuilder result = new StringBuilder();
                if (days > 0) {
                    result.append(days).append("n"); // Ngày
                }
                if (hours > 0 || days > 0) {
                    result.append(String.format("%dh", hours)); // Giờ
                }
                result.append(String.format("%02dp", minutes)); // Phút

                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "N/A";
            }
        }

    }
}



//package com.example.train_app.adapters.recyclerView;
//
//
//
//import android.os.Build;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.train_app.R;
//import com.example.train_app.model.Trip;
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//public class TrainAdapter extends RecyclerView.Adapter<TrainAdapter.TrainViewHolder> {
//    private final List<Trip> trips;
//    private final OnItemClickListener onTrainClickListener;
//
//    public interface OnItemClickListener {
//        void onItemClick(Trip trip);
//    }
//
//    public TrainAdapter(List<Trip> trips, OnItemClickListener listener) {
//        this.trips = trips;
//        this.onTrainClickListener = listener;
//    }
//
//    @NonNull
//    @Override
//    public TrainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
//        return new TrainViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TrainViewHolder holder, int position) {
//        Trip trip = trips.get(position);
//        holder.bind(trip);
//        holder.itemView.setOnClickListener(v -> onTrainClickListener.onItemClick(trip));
//    }
//
//    @Override
//    public int getItemCount() {
//        return trips.size();
//    }
//
//    public class TrainViewHolder extends RecyclerView.ViewHolder {
//
//        private TextView txtDepartureTime, txtDepartureStation, txtArrivalTime,
//                txtArrivalStation, txtDuration, txtTrainName, txtAvailableSeats;
//        private Button btnSelect;
//        private ImageView imgArrow;
//        private TextView tvTrainName, tvDepartureTime, tvDuration, tvArrivalTime;
//        private ImageView ivTrainIcon;
//
//        public TrainViewHolder(@NonNull View itemView) {
//            super(itemView);
////            tvTrainName = itemView.findViewById(R.id.txt_train_name);
////           // ivTrainIcon = itemView.findViewById(R.id.ivTrainIcon);
////            tvDepartureTime = itemView.findViewById(R.id.txt_departure_time);
////            tvDuration = itemView.findViewById(R.id.txt_duration);
////            tvArrivalTime = itemView.findViewById(R.id.txt_arrival_time);
//            txtDepartureTime = itemView.findViewById(R.id.txt_departure_time);
//            txtDepartureStation = itemView.findViewById(R.id.txt_departure_station);
//            txtArrivalTime = itemView.findViewById(R.id.txt_arrival_time);
//            txtArrivalStation = itemView.findViewById(R.id.txt_arrival_station);
//            txtDuration = itemView.findViewById(R.id.txt_duration);
//            txtTrainName = itemView.findViewById(R.id.txt_train_name);
//            txtAvailableSeats = itemView.findViewById(R.id.txt_available_seats);
//            btnSelect = itemView.findViewById(R.id.btn_select);
//
//        }
//
//        public void bind(Trip trip) {
//
//            DateTimeFormatter timeFormatter = null; // Định dạng 24h, ví dụ: 04:30
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                String departureTime = trip.getDepartureTime().format(timeFormatter);
//                txtDepartureTime.setText(departureTime);
//            }
//
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                String arrivalTime = trip.getArrivalTime().format(timeFormatter);
//                txtArrivalTime.setText(arrivalTime);
//            }
//
//            txtDepartureStation.setText(trip.getDepartureStation());
//            txtArrivalStation.setText(trip.getArrivalStation());
//
//            String duration = calculateDuration(trip.getDepartureTime(), trip.getArrivalTime());
//
//            txtDuration.setText(duration);
//            txtTrainName.setText(trip.getTrainName());
//            txtAvailableSeats.setText("Còn " + trip.getAvailableSeats() + " chỗ");
//
//            // Set click listener for the select button
//            btnSelect.setOnClickListener(v -> {
//                if (onTrainClickListener != null) {
//                    onTrainClickListener.onItemClick(trip);
//                }
//            });
//        }
//        private String calculateDuration(LocalDateTime start, LocalDateTime end) {
//            if (start == null || end == null) return "N/A";
//            Duration duration = null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                duration = Duration.between(start, end);
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                if (duration.isNegative()) return "Thời gian không hợp lệ";
//            }
//
//            long days = 0;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                days = duration.toDays();
//            }
//            long hours = 0;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                hours = duration.toHours() % 24;
//            }
//            long minutes = 0;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                minutes = duration.toMinutes() % 60;
//            }
//
//            StringBuilder result = new StringBuilder();
//            if (days > 0) {
//                result.append(days).append("n");
//            }
//            if (hours > 0 || days > 0) { // Hiển thị giờ nếu có ngày hoặc giờ
//                result.append(String.format("%dh", hours));
//            }
//            result.append(String.format("%02dp", minutes)); // Luôn hiển thị phút, 2 chữ số
//
//            return result.toString();
//        }
//    }
//}