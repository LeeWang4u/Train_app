    package com.example.train_app;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;

    import androidx.activity.EdgeToEdge;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;

    import com.example.train_app.activities.SelectSeatActivity;
    import com.example.train_app.dto.request.TripSeatRequestDTO;

    public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            setContentView(R.layout.activity_main);
            findViewById(R.id.button_to_second).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Khi nút được nhấn, mở SecondActivity
                    TripSeatRequestDTO tripSeatRequestDTO = new TripSeatRequestDTO(19,"Hà Nội","Sài Gòn");
                    Intent intent = new Intent(MainActivity.this, SelectSeatActivity.class);
                    intent.putExtra("trip_request", tripSeatRequestDTO);
                    startActivity(intent);
                }
            });
        }
    }
