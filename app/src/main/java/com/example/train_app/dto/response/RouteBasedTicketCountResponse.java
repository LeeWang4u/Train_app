package com.example.train_app.dto.response;

import com.google.gson.annotations.SerializedName;

public class RouteBasedTicketCountResponse {
    @SerializedName("departureStation")
    private String from;

    @SerializedName("arrivalStation")
    private String to;

    @SerializedName("ticketCount")
    private int ticketsCount;

    public RouteBasedTicketCountResponse(String from, String to, int ticketsCount) {
        this.from = from;
        this.to = to;
        this.ticketsCount = ticketsCount;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getTicketsCount() {
        return ticketsCount;
    }

    public void setTicketsCount(int ticketsCount) {
        this.ticketsCount = ticketsCount;
    }
}
