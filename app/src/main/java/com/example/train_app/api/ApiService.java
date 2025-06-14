package com.example.train_app.api;


import com.example.train_app.dto.request.PaymentRequest;
import com.example.train_app.dto.request.TripSeatRequestDTO;
import com.example.train_app.dto.response.PaymentResponse;
import com.example.train_app.dto.response.RouteBasedTicketCountResponse;
import com.example.train_app.dto.response.SearchTicketResponse;
import com.example.train_app.dto.response.Ticket;
import com.example.train_app.dto.response.TripAvailabilityResponseDTO;
import com.example.train_app.container.request.TripRequest;
import com.example.train_app.model.Station;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import com.example.train_app.dto.request.ReservationCodeRequestDTO;
import com.example.train_app.dto.request.TicketReservationReqDTO;
import com.example.train_app.dto.response.TicketType;
import com.example.train_app.dto.response.BookingResponse;
import com.example.train_app.dto.response.TicketResponseDTO;
import com.example.train_app.model.Trip;

public interface ApiService {


    @GET("station/all")
    Call<List<Station>> getAllStations();
    // Trip
    @POST("trips/searchs")
    Call<List<Trip>> searchTripsByStationsAndDate(@Body TripRequest tripRequest);

    @POST("tickets/confirmTicket")
    Call<BookingResponse> confirmTicket(@Body ReservationCodeRequestDTO reservationCodeRequestDTO);

    @POST("tickets/reserve")
    Call<TicketResponseDTO> reserveTicket(@Body TicketReservationReqDTO ticketReservationReqDTO);

    @POST("tickets/deleteReserve")
    Call<BookingResponse> deleteReserve(@Body TicketReservationReqDTO ticketReservationReqDTO);

    @GET("tickets/getTicketType")
    Call<List<TicketType>> getTicketTypes();

    @POST("carriages/seats")
    Call<TripAvailabilityResponseDTO> getCarriagesAndSeat(@Body TripSeatRequestDTO tripSeatRequestDTO);

    @GET("tickets/searchTicketByIdAndroid")
    Call<SearchTicketResponse> searchTicketById(@Query("ticketId") int ticketId);

    @GET("tickets/searchTicketByReservationCodeAndroid")
    Call<List<SearchTicketResponse>> searchTicketByReservationCodeAndroid(@Query("reservationCode") int reservationCode);

    @POST("/payment")
    Call<PaymentResponse> createPayment(@Body PaymentRequest request);

    //Statistics
    @GET("tickets/ticket-summary")
    Call<List<RouteBasedTicketCountResponse>> getTicketCountByRoutes();
}