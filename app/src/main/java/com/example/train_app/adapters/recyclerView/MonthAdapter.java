package com.example.train_app.adapters.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.train_app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MonthViewHolder> {
    private Context context;
    private List<Calendar> months;
    private Calendar selectedDate;
    private OnDateSelectedListener onDateSelectedListener;

    public interface OnDateSelectedListener {
        void onDateSelected(Calendar selectedDate);
    }

    public MonthAdapter(Context context, List<Calendar> months, Calendar selectedDate, OnDateSelectedListener listener) {
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
    public MonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_item, parent, false);
        return new MonthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MonthViewHolder holder, int position) {
        Calendar month = months.get(position);
        holder.bind(month);
    }

    @Override
    public int getItemCount() {
        return months.size();
    }

    public class MonthViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMonth;
        private GridView gvDays;
        private DateAdapter dateAdapter;

        public MonthViewHolder(View itemView) {
            super(itemView);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            gvDays = itemView.findViewById(R.id.rvDays);
        }

        public void bind(Calendar month) {
            // Cập nhật tiêu đề tháng
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM, yyyy", new Locale("vi"));
//            tvMonth.setText("Tháng " + monthFormat.format(month.getTime()));
            tvMonth.setText(monthFormat.format(month.getTime()));

            // Hiển thị ngày cho tháng
            dateAdapter = new DateAdapter(month);
            gvDays.setAdapter(dateAdapter);

            // Đảm bảo GridView hiển thị số hàng hợp lý
            int totalDays = dateAdapter.getCount();
            int rows = (int) Math.ceil((double) totalDays / 7);
            ViewGroup.LayoutParams params = gvDays.getLayoutParams();
            params.height = rows * 48; // 48dp mỗi hàng
            gvDays.setLayoutParams(params);
        }
    }

    private class DateAdapter extends BaseAdapter {
        private List<Date> dates;
        private Calendar calendar;
        private int daysInMonth;
        private int firstDayOfWeek;

        public DateAdapter(Calendar month) {
            this.calendar = (Calendar) month.clone();
            this.dates = new ArrayList<>();

            // Đặt ngày đầu tiên của tháng
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            // Tính toán ngày đầu tuần (Thứ 2 là ngày đầu tuần)
            int emptyDays = firstDayOfWeek - Calendar.MONDAY;
            if (emptyDays < 0) emptyDays += 7;

            // Thêm các ngày trống đầu tháng
            for (int i = 0; i < emptyDays; i++) {
                dates.add(null);
            }

            // Thêm các ngày trong tháng
            for (int i = 1; i <= daysInMonth; i++) {
                Calendar day = (Calendar) calendar.clone();
                day.set(Calendar.DAY_OF_MONTH, i);
                dates.add(day.getTime());
            }

            // Thêm các ngày trống cuối tháng (nếu cần)
            int totalCells = (int) Math.ceil((emptyDays + daysInMonth) / 7.0) * 7;
            while (dates.size() < totalCells) {
                dates.add(null);
            }
        }

        @Override
        public int getCount() {
            return dates.size();
        }

        @Override
        public Object getItem(int position) {
            return dates.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.date_item, parent, false);
            }

            TextView tvDate = (TextView) convertView;
            Date date = dates.get(position);

            if (date == null) {
                tvDate.setText("");
                tvDate.setClickable(false);
                tvDate.setBackgroundResource(android.R.color.transparent);
            } else {
                Calendar day = Calendar.getInstance();
                day.setTime(date);
                tvDate.setText(String.valueOf(day.get(Calendar.DAY_OF_MONTH)));

                // Kiểm tra ngày có nằm trong quá khứ không
                Calendar today = Calendar.getInstance();
                today.set(Calendar.HOUR_OF_DAY, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                today.set(Calendar.MILLISECOND, 0);

                if (day.before(today)) {
                    tvDate.setEnabled(false);
                    tvDate.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
                    tvDate.setBackgroundResource(R.drawable.normal_date_bg);
                } else {
                    tvDate.setEnabled(true);
                    tvDate.setTextColor(ContextCompat.getColor(context, android.R.color.black));

                    // Kiểm tra ngày có được chọn không
                    boolean isSelected = selectedDate != null &&
                            day.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) &&
                            day.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) &&
                            day.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH);

                    tvDate.setSelected(isSelected);

                    tvDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Calendar newSelectedDate = Calendar.getInstance();
                            newSelectedDate.setTime(date);
                            selectedDate = newSelectedDate;
                            onDateSelectedListener.onDateSelected(selectedDate);
                            notifyDataSetChanged();
                        }
                    });
                }
            }

            return convertView;
        }
        // ... giữ nguyên các phương thức khác
    }


}


