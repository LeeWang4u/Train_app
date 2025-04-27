package com.example.train_app.dto.request;

import java.io.Serializable;

public class TripSeatRequestDTO implements Serializable {
    private int tripId;
    private String departureStation;
    private String arrivalStation;

    public TripSeatRequestDTO(int tripId, String departureStation, String arrivalStation) {
        this.tripId = tripId;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(String departureStation) {
        this.departureStation = departureStation;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(String arrivalStation) {
        this.arrivalStation = arrivalStation;
    }
}