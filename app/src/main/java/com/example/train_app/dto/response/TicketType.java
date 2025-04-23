package com.example.train_app.dto.response;

import java.math.BigDecimal;


public class TicketType {
    private int ticketTypeId;

    private String ticketTypeName;

    private BigDecimal discountRate;

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public String getTicketTypeName() {
        return ticketTypeName;
    }

    public int getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketTypeId(int ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public void setTicketTypeName(String ticketTypeName) {
        this.ticketTypeName = ticketTypeName;
    }
}
