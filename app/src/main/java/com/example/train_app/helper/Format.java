package com.example.train_app.helper;

import android.os.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Format {

    public static String formatDateToDashYMD(String inputDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatDateToDashDMY(String inputDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatLocalDateTimeToHHmm(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return "N/A";
        }
        try {
            DateTimeFormatter inputFormatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            }
            DateTimeFormatter outputFormatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                outputFormatter = DateTimeFormatter.ofPattern("HH:mm");
            }
            LocalDateTime dateTime = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dateTime = LocalDateTime.parse(dateTimeString, inputFormatter);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return dateTime.format(outputFormatter);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "N/A";
        }
        return "N/A";
    }
}
