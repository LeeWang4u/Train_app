package com.example.train_app.container.request;

import java.io.Serializable;

public class TripRequest implements Serializable {
    private String departureStation;
    private String arrivalStation;
    private String tripDate;

    public TripRequest(String departureStation, String arrivalStation, String tripDate) {
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.tripDate = tripDate;
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

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }
}

