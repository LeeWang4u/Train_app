package com.example.train_app.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Format {
    public static String formatSeatPrice(BigDecimal price) {
        if (price == null) {
            return "";
        }
        BigDecimal thousand = new BigDecimal(1000);
        BigDecimal valueInK = price.divide(thousand);
        return String.format("%sK", valueInK.stripTrailingZeros().toPlainString());
    }
    public static String formatPriceToVnd(BigDecimal price){
        if (price == null) {
            return "0 ₫";
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,###", symbols);

        return formatter.format(price) + " ₫";
    }
    public static String formatCompartment(int stt, String compartmentName){
        return "Toa số " + String.valueOf(stt) + " - "+ compartmentName;
    }
}
