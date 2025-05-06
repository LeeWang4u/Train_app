package com.example.train_app.payment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.train_app.dto.request.CustomerDTO;
import com.example.train_app.utils.Format;
import com.example.train_app.utils.ReservationSeat;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import vn.momo.momo_partner.AppMoMoLib;

public class MomoPaymentHandler {
    private String merchantName = "HoangNgoc";
    private String merchantCode = "MOMOC2IC20220501";
    private String description = "Thanh toán đặt vé tàu online";

    public interface MomoPaymentCallback {
        void onSuccess(String token, String phoneNumber);
        void onFailure(String message);
        void onCancel();
    }

    private MomoPaymentCallback callback;

    public void requestPayment(Activity activity, CustomerDTO customerDTO, MomoPaymentCallback callback) {
        this.callback = callback;
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);

        String amount = Format.formatPriceToVnd(ReservationSeat.getFinalTotalPrice());
        String orderId = "DSVN" + System.currentTimeMillis();
        String orderLabel = customerDTO.getFullName() + System.currentTimeMillis();

        Map<String, Object> eventValue = new HashMap<>();
        eventValue.put("merchantname", merchantName);
        eventValue.put("merchantcode", merchantCode);
        eventValue.put("amount", ReservationSeat.getFinalTotalPrice().setScale(0, RoundingMode.HALF_UP).intValueExact());
        eventValue.put("orderId", orderId);
        eventValue.put("orderLabel", orderLabel);
        eventValue.put("merchantnamelabel", "Dịch vụ bán vé tàu");
        eventValue.put("fee", 0);
        eventValue.put("description", description);
        eventValue.put("requestId", merchantCode + "merchant_billId_" + System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);

        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());
        eventValue.put("extra", "");

        AppMoMoLib.getInstance().requestMoMoCallBack(activity, eventValue);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                int status = data.getIntExtra("status", -1);
                if (status == 0) {
                    String token = data.getStringExtra("data");
                    String phoneNumber = data.getStringExtra("phonenumber");
                    if (token != null && !token.isEmpty()) {
                        if (callback != null) callback.onSuccess(token, phoneNumber);
                    } else {
                        if (callback != null) callback.onFailure("Không nhận được token từ MoMo.");
                    }
                } else if (status == 1) {
                    if (callback != null) callback.onFailure(data.getStringExtra("message"));
                } else {
                    if (callback != null) callback.onCancel();
                }
            } else {
                if (callback != null) callback.onFailure("Dữ liệu phản hồi MoMo null.");
            }
        } else {
            if (callback != null) callback.onFailure("RequestCode không hợp lệ.");
        }
    }
}
