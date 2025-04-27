package com.example.train_app.dto.request;

import com.example.train_app.dto.response.TicketResponseDTO;

import java.math.BigDecimal;

public class SelectSeatReqDTO {

    private int seatId;

    private String compartmentName;
    private String seatName;
    private int stt;



    private BigDecimal ticketPrice;
    private TicketResponseDTO ticketResponseDTO;

    public SelectSeatReqDTO(int seatId, BigDecimal ticketPrice, int stt, String seatName,String compartmentName) {
        this.seatId = seatId;
        this.ticketPrice = ticketPrice;
        this.stt = stt;
        this.seatName = seatName;
        this.compartmentName=compartmentName;
    }

    public String getCompartmentName() {
        return compartmentName;
    }

    public void setCompartmentName(String compartmentName) {
        this.compartmentName = compartmentName;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public String getSeatName() {
        return seatName;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public TicketResponseDTO getTicketResponseDTO() {
        return ticketResponseDTO;
    }

    public void setTicketResponseDTO(TicketResponseDTO ticketResponseDTO) {
        this.ticketResponseDTO = ticketResponseDTO;
    }
}
