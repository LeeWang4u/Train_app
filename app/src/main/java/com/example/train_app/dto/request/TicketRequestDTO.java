package com.example.train_app.dto.request;

import com.example.train_app.dto.response.TicketType;

import java.math.BigDecimal;



public class TicketRequestDTO {

    private int ticketId;

    private BigDecimal discount;

    private String fullName;

    private String cccd;

    private BigDecimal price;

    private BigDecimal totalPrice;

    private int seat;

    private int trip;

    private String arrivalStation;

    private String departureStation;

    private TicketType ticketType;

    public int getTicketId() {
        return ticketId;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public int getTrip() {
        return trip;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public int getSeat() {
        return seat;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCccd() {
        return cccd;
    }

    public String getFullName() {
        return fullName;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public void setDepartureStation(String departureStation) {
        this.departureStation = departureStation;
    }

    public void setArrivalStation(String arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public void setTrip(int trip) {
        this.trip = trip;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public TicketRequestDTO() {
    }

    public TicketRequestDTO(int ticketId, TicketType ticketType, String departureStation, String arrivalStation, int trip, int seat, BigDecimal totalPrice, BigDecimal price, String cccd, String fullName, BigDecimal discount) {
        this.ticketId = ticketId;
        this.ticketType = ticketType;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.trip = trip;
        this.seat = seat;
        this.totalPrice = totalPrice;
        this.price = price;
        this.cccd = cccd;
        this.fullName = fullName;
        this.discount = discount;
    }
}
