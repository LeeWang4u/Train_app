package com.example.train_app.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;

public class SeatAvailabilityResponseDTO implements Serializable {
    private int seatId;
    private String seatNumber;
    private int floor;
    private BigDecimal seatFactor;
    private String seatStatus;
    private boolean available;
    private BigDecimal ticketPrice; // Thêm trường giá vé

    public SeatAvailabilityResponseDTO(int seatId, String seatNumber, int floor, BigDecimal seatFactor,
                                       String seatStatus, boolean available, BigDecimal ticketPrice) {
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.floor = floor;
        this.seatFactor = seatFactor;
        this.seatStatus = seatStatus;
        this.available = available;
        this.ticketPrice = ticketPrice;
    }

    // Getter và Setter
    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public BigDecimal getSeatFactor() {
        return seatFactor;
    }

    public void setSeatFactor(BigDecimal seatFactor) {
        this.seatFactor = seatFactor;
    }

    public String getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(String seatStatus) {
        this.seatStatus = seatStatus;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}