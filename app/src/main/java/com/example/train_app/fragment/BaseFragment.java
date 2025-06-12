package com.example.train_app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.train_app.utils.ThemeManager;

public abstract class BaseFragment extends Fragment {

    protected ThemeManager themeManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Khởi tạo ThemeManager tại đây — tương đương với BaseActivity
        themeManager = new ThemeManager(context);
        themeManager.applyTheme();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyThemeToView(view);
    }

    /**
     * Các Fragment con có thể override hàm này để áp dụng theme cho view.
     */
    protected void applyThemeToView(@NonNull View view) {
        // Override ở Fragment con nếu muốn xử lý
    }
}
