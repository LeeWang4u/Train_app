package com.example.train_app.activities;

import android.content.Intent;
import android.os.Bundle;
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

public class SelectDateActivity extends AppCompatActivity
        implements MonthPagerAdapter.OnDateSelectedListener {
    private TextView tvTitle, tvDepartureDate, tvMonth;
    private ImageButton btnBack, btnPrevMonth, btnNextMonth;
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

        // initialize views
        tvTitle = findViewById(R.id.tvTitle);
        tvDepartureDate = findViewById(R.id.tvDepartureDate);
        tvMonth = findViewById(R.id.tvMonth);
        btnBack = findViewById(R.id.btnBack);
        btnPrevMonth = findViewById(R.id.btnPrevMonth);
        btnNextMonth = findViewById(R.id.btnNextMonth);
        vpCalendar = findViewById(R.id.vpCalendar);
        btnApply = findViewById(R.id.btnApply);

        // get selected date from intent
        String dateStr = getIntent().getStringExtra("selected_date");
        selectedDate = Calendar.getInstance();
        if (dateStr != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                selectedDate.setTime(sdf.parse(dateStr));
            } catch (Exception ignored) {}
        }

        // display selected date
        SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvDepartureDate.setText(sdfDisplay.format(selectedDate.getTime()));

        // prepare next 12 months
        months = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        for (int i = 0; i < 12; i++) {
            months.add((Calendar) cal.clone());
            cal.add(Calendar.MONTH, 1);
        }

        // find initial position
        currentMonthPosition = 0;
        for (int i = 0; i < months.size(); i++) {
            Calendar m = months.get(i);
            if (m.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR)
                    && m.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH)) {
                currentMonthPosition = i;
                break;
            }
        }

        // setup pager
        monthPagerAdapter = new MonthPagerAdapter(this, months, selectedDate, this);
        vpCalendar.setAdapter(monthPagerAdapter);
        vpCalendar.setCurrentItem(currentMonthPosition);
        updateMonthTitle();

        vpCalendar.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int pos) {
                currentMonthPosition = pos;
                updateMonthTitle();
            }
        });

        btnPrevMonth.setOnClickListener(v -> {
            if (currentMonthPosition > 0) vpCalendar.setCurrentItem(--currentMonthPosition);
        });
        btnNextMonth.setOnClickListener(v -> {
            if (currentMonthPosition < months.size() - 1) vpCalendar.setCurrentItem(++currentMonthPosition);
        });
        btnBack.setOnClickListener(v -> finish());
        btnApply.setOnClickListener(v -> {
            Intent result = new Intent();
            result.putExtra("selected_date", sdfDisplay.format(selectedDate.getTime()));
            setResult(RESULT_OK, result);
            finish();
        });
    }

    private void updateMonthTitle() {
        Calendar month = months.get(currentMonthPosition);
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MMMM yyyy", new Locale("vi", "VN"));
        String title = sdfMonth.format(month.getTime());
        tvMonth.setText(Character.toUpperCase(title.charAt(0)) + title.substring(1));
    }

    @Override
    public void onDateSelected(Calendar date) {
        selectedDate = date;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvDepartureDate.setText(sdf.format(date.getTime()));
    }
}