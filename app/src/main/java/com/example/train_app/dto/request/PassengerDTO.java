package com.example.train_app.dto.request;


import com.example.train_app.dto.response.TicketType;

public class PassengerDTO {

    private String Cccd;

    private String FullName;

    private TicketType ticketType;

    public void setCccd(String cccd) {
        Cccd = cccd;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public String getFullName() {
        return FullName;
    }

    public String getCccd() {
        return Cccd;
    }

    public PassengerDTO() {
    }

    public PassengerDTO(String cccd, TicketType ticketType, String fullName) {
        Cccd = cccd;
        this.ticketType = ticketType;
        FullName = fullName;
    }
}
