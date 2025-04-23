package com.example.train_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.train_app.R;
import com.example.train_app.fragment.Coach4BedsFragment;
import com.example.train_app.fragment.Coach6BedsFragment;
import com.example.train_app.fragment.CoachSeatFragment;

import java.util.Arrays;
import java.util.List;


public class SelectSeatActivity extends AppCompatActivity {

    private LinearLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seat);

        fragmentContainer = findViewById(R.id.fragment_container);

        // Danh sách loại coach, có thể thay bằng dữ liệu động
        List<String> coachTypes = Arrays.asList("seat", "bed6","bed4");

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            for (String type : coachTypes) {
                // Tạo FrameLayout động cho từng fragment
                FrameLayout frame = new FrameLayout(this);
                int viewId = View.generateViewId();
                frame.setId(viewId);

                // Thêm layout container vào LinearLayout
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                lp.setMargins(0, 16, 0, 16);
                fragmentContainer.addView(frame, lp);

                // Tạo fragment tương ứng
                Fragment frag;

                switch (type) {
                    case "seat":
                        frag = new CoachSeatFragment();
                        break;
                    case "bed6":
                        frag = new Coach6BedsFragment();
                        break;
                    case "bed4":
                        frag = new Coach4BedsFragment();
                        break;
                    default:
                        frag = new CoachSeatFragment(); // fallback nếu type không hợp lệ
                        break;
                }
                ft.add(viewId, frag);
            }

            ft.commit();
        }

        // Nút Continue
        Button continueButton = findViewById(R.id.btn_continue);
        continueButton.setOnClickListener(v -> {
            Intent intent = new Intent(SelectSeatActivity.this, InfoActivity.class);
            startActivity(intent);
        });
    }
}

