package com.example.train_app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.train_app.R;

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = "PaymentActivity";
    private String paymentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Check if the activity was opened via a deep link (e.g., after payment redirect)
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null && data.toString().startsWith("app_name://payment-success")) {
            Log.d(TAG, "Received redirect from browser: " + data.toString());
            // Navigate to SearchTrainActivity
            Intent redirectIntent = new Intent(PaymentActivity.this, SearchTrainActivity.class);
            startActivity(redirectIntent);
            finish();
            return;
        }

        // Get the payment URL from the intent
        paymentUrl = getIntent().getStringExtra("payment_url");
        if (paymentUrl == null || paymentUrl.isEmpty()) {
            Toast.makeText(this, "Payment URL is missing", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Log.d(TAG, "Payment URL: " + paymentUrl);

        // Open the payment URL in the default browser
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl));
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(browserIntent);
            // Keep PaymentActivity alive to handle the redirect
        } catch (Exception e) {
            Log.e(TAG, "Error opening browser: " + e.getMessage());
            Toast.makeText(this, "Unable to open browser for payment", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        // Handle the redirect if the activity is already running
        Uri data = intent.getData();
        if (data != null && data.toString().startsWith("app_name://payment-success")) {
            Log.d(TAG, "Received redirect from browser (onNewIntent): " + data.toString());
            Intent redirectIntent = new Intent(PaymentActivity.this, SearchTrainActivity.class);
            startActivity(redirectIntent);
            finish();
        }
    }
}