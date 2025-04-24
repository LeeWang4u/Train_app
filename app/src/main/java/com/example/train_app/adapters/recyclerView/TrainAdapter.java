package com.example.train_app.adapters.recyclerView;



import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.train_app.R;
import com.example.train_app.model.Trip;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TrainAdapter extends RecyclerView.Adapter<TrainAdapter.TrainViewHolder> {
    private List<Trip> trips;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Trip trip);
    }

    public TrainAdapter(List<Trip> trips, OnItemClickListener listener) {
        this.trips = trips;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.train_item_2, parent, false);
        return new TrainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainViewHolder holder, int position) {
        Trip trip = trips.get(position);
        holder.bind(trip);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(trip));
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    static class TrainViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTrainName, tvDepartureTime, tvDuration, tvArrivalTime;
        private ImageView ivTrainIcon;

        public TrainViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTrainName = itemView.findViewById(R.id.txt_train_name);
           // ivTrainIcon = itemView.findViewById(R.id.ivTrainIcon);
            tvDepartureTime = itemView.findViewById(R.id.txt_departure_time);
            tvDuration = itemView.findViewById(R.id.txt_duration);
            tvArrivalTime = itemView.findViewById(R.id.txt_arrival_time);
        }

        public void bind(Trip trip) {
            tvTrainName.setText(trip.getTrainName());
           // ivTrainIcon.setImageResource(R.drawable.head_train_icon);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tvDepartureTime.setText(trip.getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            }
            Duration duration = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                duration = Duration.between(trip.getDepartureTime(), trip.getArrivalTime());
            }
            long days = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                days = duration.toDays();
            }
            long hours = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                hours = duration.toHours() % 24;
            }
            long minutes = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                minutes = duration.toMinutes() % 60;
            }
            tvDuration.setText(String.format("%dn %dg %dp", days, hours, minutes));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tvArrivalTime.setText(trip.getArrivalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            }
        }
    }
}