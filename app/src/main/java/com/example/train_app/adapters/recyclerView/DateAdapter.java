//package com.example.train_app.adapters.recyclerView;
//
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.train_app.R;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {
//    private final List<Date> dates;
//    private int selectedPos = RecyclerView.NO_POSITION;
//    private final OnDateSelectedListener listener;
//
//    public interface OnDateSelectedListener {
//        void onDateSelected(Calendar selectedDate);
//    }
//
//    public DateAdapter(Calendar month, Calendar initDate,
//                       OnDateSelectedListener listener) {
//        this.listener = listener;
//        this.dates = new ArrayList<>();
//        Calendar cal = (Calendar) month.clone();
//        cal.set(Calendar.DAY_OF_MONTH, 1);
//
//        int offset = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7; // Mon=0
//        for (int i = 0; i < offset; i++) dates.add(null);
//        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//        for (int d = 1; d <= maxDay; d++) {
//            Calendar c = (Calendar) cal.clone();
//            c.set(Calendar.DAY_OF_MONTH, d);
//            dates.add(c.getTime());
//            if (initDate.get(Calendar.YEAR) == c.get(Calendar.YEAR)
//                    && initDate.get(Calendar.MONTH) == c.get(Calendar.MONTH)
//                    && initDate.get(Calendar.DAY_OF_MONTH) == d) {
//                selectedPos = dates.size() - 1;
//            }
//        }
//        int rows = (int) Math.ceil(dates.size() / 7.0);
//        while (dates.size() < rows * 7) dates.add(null);
//    }
//
//    @NonNull
//    @Override
//    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        TextView tv = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.date_item, parent, false);
//        return new DateViewHolder(tv);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
//        // Lấy vị trí binding ngay lập tức
//        Date date = dates.get(position);
//        if (date == null) {
//            holder.tv.setText("");
//            holder.tv.setEnabled(false);
//            holder.tv.setSelected(false);
//        } else {
//            Calendar day = Calendar.getInstance(); day.setTime(date);
//            holder.tv.setText(String.valueOf(day.get(Calendar.DAY_OF_MONTH)));
//            Calendar today = Calendar.getInstance();
//            today.set(Calendar.HOUR_OF_DAY,0);
//            today.set(Calendar.MINUTE,0);
//            today.set(Calendar.SECOND,0);
//            today.set(Calendar.MILLISECOND,0);
//
//            boolean past = day.before(today);
//            holder.tv.setEnabled(!past);
//            holder.tv.setTextColor(holder.tv.getContext()
//                    .getResources().getColor(
//                            past ? android.R.color.darker_gray : android.R.color.black));
//            holder.tv.setSelected(position == selectedPos);
//
//            holder.tv.setOnClickListener(v -> {
//                int adapterPos = holder.getAdapterPosition();
//                if (adapterPos == RecyclerView.NO_POSITION) return;
//                Date clickedDate = dates.get(adapterPos);
//                // Kiểm tra ngày hợp lệ
//                Calendar clickedDay = Calendar.getInstance();
//                clickedDay.setTime(clickedDate);
//                Calendar todayCheck = Calendar.getInstance();
//                todayCheck.set(Calendar.HOUR_OF_DAY,0);
//                todayCheck.set(Calendar.MINUTE,0);
//                todayCheck.set(Calendar.SECOND,0);
//                todayCheck.set(Calendar.MILLISECOND,0);
//                if (clickedDay.before(todayCheck)) return;
//
//                int oldPos = selectedPos;
//                selectedPos = adapterPos;
//                notifyItemChanged(oldPos);
//                notifyItemChanged(selectedPos);
//                listener.onDateSelected(clickedDay);
//            });
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return dates.size();
//    }
//
//    static class DateViewHolder extends RecyclerView.ViewHolder {
//        TextView tv;
//        DateViewHolder(@NonNull TextView itemView) {
//            super(itemView);
//            tv = itemView;
//        }
//    }
//}
//
//
//



// File: DateAdapter.java
package com.example.train_app.adapters.recyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.train_app.R;
import com.example.train_app.adapters.MonthPagerAdapter.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {
    private final List<Date> dates;
    private int selectedPos = RecyclerView.NO_POSITION;
    private final OnDateSelectedListener listener;

    public DateAdapter(Calendar month, Calendar initDate,
                       OnDateSelectedListener listener) {
        this.listener = listener;
        this.dates = new ArrayList<>();
        Calendar cal = (Calendar) month.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        int offset = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7; // Mon=0
        for (int i = 0; i < offset; i++) dates.add(null);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int d = 1; d <= maxDay; d++) {
            Calendar c = (Calendar) cal.clone();
            c.set(Calendar.DAY_OF_MONTH, d);
            dates.add(c.getTime());
            if (initDate.get(Calendar.YEAR) == c.get(Calendar.YEAR)
                    && initDate.get(Calendar.MONTH) == c.get(Calendar.MONTH)
                    && initDate.get(Calendar.DAY_OF_MONTH) == d) {
                selectedPos = dates.size() - 1;
            }
        }
        int rows = (int) Math.ceil(dates.size() / 7.0);
        while (dates.size() < rows * 7) dates.add(null);
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView tv = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_item, parent, false);
        return new DateViewHolder(tv);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        Date date = dates.get(position);
        if (date == null) {
            holder.tv.setText("");
            holder.tv.setEnabled(false);
            holder.tv.setSelected(false);
        } else {
            Calendar day = Calendar.getInstance();
            day.setTime(date);
            holder.tv.setText(String.valueOf(day.get(Calendar.DAY_OF_MONTH)));

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            boolean past = day.before(today);
            holder.tv.setEnabled(!past);
            holder.tv.setTextColor(holder.tv.getContext()
                    .getResources().getColor(
                            past ? android.R.color.darker_gray : android.R.color.black));
            holder.tv.setSelected(position == selectedPos);

            holder.tv.setOnClickListener(v -> {
                int adapterPos = holder.getAdapterPosition();
                if (adapterPos == RecyclerView.NO_POSITION) return;

                Date clickedDate = dates.get(adapterPos);
                Calendar clickedDay = Calendar.getInstance();
                clickedDay.setTime(clickedDate);
                Calendar todayCheck = Calendar.getInstance();
                todayCheck.set(Calendar.HOUR_OF_DAY,0);
                todayCheck.set(Calendar.MINUTE,0);
                todayCheck.set(Calendar.SECOND,0);
                todayCheck.set(Calendar.MILLISECOND,0);
                if (clickedDay.before(todayCheck)) return;

                int old = selectedPos;
                selectedPos = adapterPos;
                notifyItemChanged(old);
                notifyItemChanged(selectedPos);
                listener.onDateSelected(clickedDay);
            });
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        DateViewHolder(@NonNull TextView itemView) {
            super(itemView);
            tv = itemView;
        }
    }
}
