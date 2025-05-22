package com.example.train_app.utils;

import com.example.train_app.model.Trip;

public class CurrentTrip {
    public static Trip currentTrip = new Trip()  ;

    public static void setTrip(Trip trip) {
        currentTrip = trip;
    }
    public static Trip getCurrentTrip(){
        return currentTrip;
    }
}
