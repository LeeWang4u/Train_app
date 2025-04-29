package com.example.train_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.train_app.R;
import com.example.train_app.adapters.recyclerView.DateAdapter;

import java.util.Calendar;
import java.util.List;

public class MonthPagerAdapter extends PagerAdapter {
    public interface OnDateSelectedListener {
        void onDateSelected(Calendar selectedDate);
    }

    private final Context context;
    private final List<Calendar> months;
    private final Calendar initialDate;
    private final OnDateSelectedListener listener;

    public MonthPagerAdapter(Context context,
                             List<Calendar> months,
                             Calendar initialDate,
                             OnDateSelectedListener listener) {
        this.context = context;
        this.months = months;
        this.initialDate = (Calendar) initialDate.clone();
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return months.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View page = LayoutInflater.from(context)
                .inflate(R.layout.month_item, container, false);
        RecyclerView rv = page.findViewById(R.id.rvDays);
        DateAdapter adapter = new DateAdapter(months.get(position), initialDate, listener);
        rv.setLayoutManager(new GridLayoutManager(context, 7));
        rv.setAdapter(adapter);
        container.addView(page);
        return page;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}