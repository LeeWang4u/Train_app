package com.example.train_app.dto.request;


public class TicketReservationReqDTO {



    private int seat;

    private int trip;

    private String arrivalStation;

    private String departureStation;

    public int getSeat() {
        return seat;
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

    public void setSeat(int seat) {
        this.seat = seat;
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

    public TicketReservationReqDTO() {
    }

    public TicketReservationReqDTO(int seat, String departureStation, String arrivalStation, int trip) {
        this.seat = seat;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.trip = trip;
    }
}