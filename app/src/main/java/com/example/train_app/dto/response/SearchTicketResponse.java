package com.example.train_app.dto.response;

public class SearchTicketResponse {
    private int ticketId;
    private String passengerName;
    private String departureStation;
    private String arrivalStation;
    private String seatName;
    private String trainName;

    public SearchTicketResponse(int ticketId, String trainName, String seatName, String arrivalStation, String departureStation, String passengerName) {
        this.ticketId = ticketId;
        this.trainName = trainName;
        this.seatName = seatName;
        this.arrivalStation = arrivalStation;
        this.departureStation = departureStation;
        this.passengerName = passengerName;
    }

    public SearchTicketResponse() {

    }

    public int getTicketId() {
        return ticketId;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getseatName() {
        return seatName;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public void setseatName(String seatName) {
        this.seatName = seatName;
    }

    public void setArrivalStation(String arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public void setDepartureStation(String departureStation) {
        this.departureStation = departureStation;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }
}
