package com.example.train_app.activities;


import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.train_app.utils.ThemeManager;

public class BaseActivity extends AppCompatActivity {
    private ThemeManager themeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Khởi tạo ThemeManager và áp dụng theme trước khi gọi super.onCreate
        themeManager = new ThemeManager(this);
        themeManager.applyTheme();
        super.onCreate(savedInstanceState);
    }

    public ThemeManager getThemeManager() {
        return themeManager;
    }

    public boolean isDarkMode() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}