package com.example.train_app.utils;

import com.example.train_app.dto.request.SelectSeatReqDTO;
import com.example.train_app.dto.response.SeatAvailabilityResponseDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReservationSeat {
    public static List<SelectSeatReqDTO> selectedSeats = new ArrayList<>();
    public static BigDecimal totalPrice=BigDecimal.ZERO;

    public static BigDecimal finalTotalPrice = BigDecimal.ZERO;
    public static void addSeat(SelectSeatReqDTO seat) {
        selectedSeats.add(seat);
       totalPrice = totalPrice.add(seat.getTicketPrice());
    }
    public static void removeSeat(SelectSeatReqDTO seat) {
        selectedSeats.removeIf(s -> s.getSeatId()==seat.getSeatId());
        totalPrice = totalPrice.subtract(seat.getTicketPrice());
    }
    public static void clearSelectedSeats(){
        selectedSeats.clear();
    }
    public static List<SelectSeatReqDTO> getSelectedSeats(){
        return selectedSeats;
    }
    public static boolean checkSelectedSeat(SelectSeatReqDTO seat) {
        return selectedSeats.stream().anyMatch(s -> s.getSeatId() == seat.getSeatId());
    }

    public static int sumSelectedSeat() {
        return selectedSeats.size();
    }

    public static BigDecimal getTotalPrice(){
        return totalPrice;
    }
    public static void setFinalTotalPrice(BigDecimal price, BigDecimal finalPrice){
        finalTotalPrice = getTotalPrice().subtract(price).add(finalPrice);
    }
    public static BigDecimal getFinalTotalPrice(){
        return finalTotalPrice;
    }
}
