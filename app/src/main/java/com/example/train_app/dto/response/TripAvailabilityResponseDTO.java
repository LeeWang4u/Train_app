package com.example.train_app.dto.response;

import java.io.Serializable;
import java.util.List;

public class TripAvailabilityResponseDTO implements Serializable {
    private int tripId;
    private List<CarriageAvailabilityResponseDTO> carriages;

    public TripAvailabilityResponseDTO(int tripId, List<CarriageAvailabilityResponseDTO> carriages) {
        this.tripId = tripId;
        this.carriages = carriages;
    }

    // Getter và Setter
    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public List<CarriageAvailabilityResponseDTO> getCarriages() {
        return carriages;
    }

    public void setCarriages(List<CarriageAvailabilityResponseDTO> carriages) {
        this.carriages = carriages;
    }
}
