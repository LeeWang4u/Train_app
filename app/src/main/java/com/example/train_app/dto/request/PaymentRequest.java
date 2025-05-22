package com.example.train_app.dto.request;

import java.math.BigDecimal;

public class PaymentRequest {
    private String customer;
    private BigDecimal amount;

    public PaymentRequest(String customer, BigDecimal amount) {
        this.customer = customer;
        this.amount = amount;
    }
}