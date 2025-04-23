package com.example.train_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.train_app.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MonthPagerAdapter extends PagerAdapter {
    private Context context;
    private List<Calendar> months;
    private Calendar selectedDate;
    private OnDateSelectedListener onDateSelectedListener;

    public interface OnDateSelectedListener {
        void onDateSelected(Calendar selectedDate);
    }

    public MonthPagerAdapter(Context context, List<Calendar> months, Calendar selectedDate, OnDateSelectedListener listener) {
        this.context = context;
        this.months = months;
        this.selectedDate = selectedDate;
        this.onDateSelectedListener = listener;
    }

    public void updateSelectedDate(Calendar newSelectedDate) {
        this.selectedDate = newSelectedDate;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.month_item, container, false);
        Calendar month = months.get(position);

        RecyclerView rvDays = view.findViewById(R.id.rvDays);
        DateAdapter dateAdapter = new DateAdapter(month);
        rvDays.setLayoutManager(new GridLayoutManager(context, 7)); // 7 cột cho 7 ngày
        rvDays.setAdapter(dateAdapter);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return months.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE; // Luôn làm mới khi gọi notifyDataSetChanged
    }

    private class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {
        private List<Date> dates;
        private Calendar calendar;

        public DateAdapter(Calendar month) {
            this.calendar = (Calendar) month.clone();
            this.dates = new ArrayList<>();

            // Đặt ngày đầu tiên của tháng
            calendar.set(Calendar.DAY_OF_MONTH, 1);

            // Tính số ngày trống đầu tháng (để căn chỉnh lịch)
            int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2; // Điều chỉnh để T2 là 0
            if (firstDayOfWeek < 0) firstDayOfWeek += 7;

            // Thêm các ngày trống
            for (int i = 0; i < firstDayOfWeek; i++) {
                dates.add(null);
            }

            // Thêm các ngày trong tháng
            int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (int i = 1; i <= maxDay; i++) {
                Calendar day = (Calendar) calendar.clone();
                day.set(Calendar.DAY_OF_MONTH, i);
                dates.add(day.getTime());
            }

            // Đảm bảo hiển thị đủ số hàng
            int totalSlots = dates.size(); // Tổng số ô (trống + ngày)
            int rowsNeeded = (totalSlots + 6) / 7; // Làm tròn lên để đảm bảo đủ hàng
            if (rowsNeeded < 5) rowsNeeded = 5; // Đảm bảo tối thiểu 5 hàng để hiển thị đầy đủ
            int slotsToAdd = (rowsNeeded * 7) - totalSlots; // Số ô trống cần thêm
            for (int i = 0; i < slotsToAdd; i++) {
                dates.add(null); // Thêm ô trống để đủ hàng
            }
        }

        @NonNull
        @Override
        public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.date_item, parent, false);
            return new DateViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
            Date date = dates.get(position);

            if (date == null) {
                holder.tvDate.setText("");
                holder.tvDate.setEnabled(false);
                holder.tvDate.setBackgroundResource(0);
            } else {
                Calendar day = Calendar.getInstance();
                day.setTime(date);
                holder.tvDate.setText(String.valueOf(day.get(Calendar.DAY_OF_MONTH)));

                // Kiểm tra ngày có nằm trong quá khứ không
                Calendar today = Calendar.getInstance();
                today.set(Calendar.HOUR_OF_DAY, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                today.set(Calendar.MILLISECOND, 0);

                if (day.before(today)) {
                    holder.tvDate.setEnabled(false); // Vô hiệu hóa ngày trong quá khứ
                    holder.tvDate.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                } else {
                    holder.tvDate.setEnabled(true);
                    holder.tvDate.setTextColor(context.getResources().getColor(android.R.color.black));

                    // Kiểm tra ngày có được chọn không
                    if (selectedDate != null &&
                            day.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) &&
                            day.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) &&
                            day.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH)) {
                        holder.tvDate.setSelected(true);
                    } else {
                        holder.tvDate.setSelected(false);
                    }

                    // Xử lý khi chọn ngày
                    holder.tvDate.setOnClickListener(v -> {
                        selectedDate.setTime(date);
                        onDateSelectedListener.onDateSelected(selectedDate);
                        notifyDataSetChanged(); // Cập nhật giao diện RecyclerView
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            return dates.size();
        }

        public class DateViewHolder extends RecyclerView.ViewHolder {
            TextView tvDate;

            public DateViewHolder(@NonNull View itemView) {
                super(itemView);
                tvDate = (TextView) itemView;
            }
        }
    }
}