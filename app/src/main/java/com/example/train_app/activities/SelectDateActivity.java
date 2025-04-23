package com.example.train_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.train_app.R;
import com.example.train_app.adapters.MonthPagerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SelectDateActivity extends AppCompatActivity implements MonthPagerAdapter.OnDateSelectedListener {

    private TextView tvTitle;
    private TextView tvDepartureDate;
    private ImageButton btnBack;
    private ImageButton btnPrevMonth;
    private ImageButton btnNextMonth;
    private TextView tvMonth;
    private ViewPager vpCalendar;
    private Button btnApply;

    private Calendar selectedDate;
    private List<Calendar> months;
    private MonthPagerAdapter monthPagerAdapter;
    private int currentMonthPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);

        // Khởi tạo các view
        tvTitle = findViewById(R.id.tvTitle);
        tvDepartureDate = findViewById(R.id.tvDepartureDate);
        btnBack = findViewById(R.id.btnBack);
        btnPrevMonth = findViewById(R.id.btnPrevMonth);
        btnNextMonth = findViewById(R.id.btnNextMonth);
        tvMonth = findViewById(R.id.tvMonth);
        vpCalendar = findViewById(R.id.vpCalendar);
        btnApply = findViewById(R.id.btnApply);

        // Nhận ngày được chọn từ Intent (nếu có)
        Intent intent = getIntent();
        String dateStr = intent.getStringExtra("selected_date");
        selectedDate = Calendar.getInstance();
        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                selectedDate.setTime(sdf.parse(dateStr));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Cập nhật TextView ngày đi
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvDepartureDate.setText(sdf.format(selectedDate.getTime()));

        // Tạo danh sách các tháng (12 tháng từ tháng hiện tại)
        months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1); // Đặt về đầu tháng
        for (int i = 0; i < 12; i++) {
            months.add((Calendar) calendar.clone());
            calendar.add(Calendar.MONTH, 1);
        }

        // Tìm vị trí của tháng hiện tại (dựa trên selectedDate)
        currentMonthPosition = 0;
        for (int i = 0; i < months.size(); i++) {
            Calendar month = months.get(i);
            if (month.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) &&
                    month.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH)) {
                currentMonthPosition = i;
                break;
            }
        }

        // Khởi tạo adapter cho ViewPager
        monthPagerAdapter = new MonthPagerAdapter(this, months, selectedDate, this);
        vpCalendar.setAdapter(monthPagerAdapter);
        vpCalendar.setCurrentItem(currentMonthPosition);

        // Cập nhật tiêu đề tháng
        updateMonthTitle();

        // Xử lý sự kiện chuyển tháng
        vpCalendar.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                currentMonthPosition = position;
                updateMonthTitle();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        // Xử lý nút Previous Month
        btnPrevMonth.setOnClickListener(v -> {
            if (currentMonthPosition > 0) {
                currentMonthPosition--;
                vpCalendar.setCurrentItem(currentMonthPosition);
            }
        });

        // Xử lý nút Next Month
        btnNextMonth.setOnClickListener(v -> {
            if (currentMonthPosition < months.size() - 1) {
                currentMonthPosition++;
                vpCalendar.setCurrentItem(currentMonthPosition);
            }
        });

        // Xử lý nút Back
        btnBack.setOnClickListener(v -> finish());

        // Xử lý nút Apply
        btnApply.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            SimpleDateFormat sdfResult = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            resultIntent.putExtra("selected_date", sdfResult.format(selectedDate.getTime()));
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void updateMonthTitle() {
        Calendar month = months.get(currentMonthPosition);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", new Locale("vi", "VN"));
        tvMonth.setText(sdf.format(month.getTime()));
//        tvMonth.setText("Tháng " + sdf.format(month.getTime()));
    }

    @Override
    public void onDateSelected(Calendar selectedDate) {
        this.selectedDate = selectedDate;
        monthPagerAdapter.updateSelectedDate(selectedDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvDepartureDate.setText(sdf.format(selectedDate.getTime()));
    }
}