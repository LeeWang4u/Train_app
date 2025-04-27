package com.example.train_app.container.request;

import java.io.Serializable;

public class TripDetailRequest implements Serializable {
    private int idTrip;
    private String departureStation;
    private String arrivalStation;

    public TripDetailRequest(String arrivalStation, String departureStation, int idTrip) {
        this.arrivalStation = arrivalStation;
        this.departureStation = departureStation;
        this.idTrip = idTrip;
    }

    public int getIdTrip() {
        return idTrip;
    }

    public void setIdTrip(int idTrip) {
        this.idTrip = idTrip;
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
