package com.example.train_app.container.response;

import java.io.Serializable;

public class TripResponse implements Serializable {
    private int tripId;
    private String trainName;
    private double basePrice;
    private String tripDate;
    private String tripStatus;
    private String departureStation;
    private String arrivalStation;
    private String departureTime;
    private String arrivalTime;
    private int availableSeats;

    public TripResponse(int tripId, String trainName, double basePrice, String tripStatus,
                        String tripDate, String departureStation, String arrivalStation,
                        String departureTime, String arrivalTime, int availableSeats) {
        this.tripId = tripId;
        this.trainName = trainName;
        this.basePrice = basePrice;
        this.tripStatus = tripStatus;
        this.tripDate = tripDate;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableSeats = availableSeats;
    }

    // Getter & Setter
    public int getTripId() { return tripId; }
    public void setTripId(int tripId) { this.tripId = tripId; }

    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }

    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

    public String getTripDate() { return tripDate; }
    public void setTripDate(String tripDate) { this.tripDate = tripDate; }

    public String getTripStatus() { return tripStatus; }
    public void setTripStatus(String tripStatus) { this.tripStatus = tripStatus; }

    public String getDepartureStation() { return departureStation; }
    public void setDepartureStation(String departureStation) { this.departureStation = departureStation; }

    public String getArrivalStation() { return arrivalStation; }
    public void setArrivalStation(String arrivalStation) { this.arrivalStation = arrivalStation; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
}

