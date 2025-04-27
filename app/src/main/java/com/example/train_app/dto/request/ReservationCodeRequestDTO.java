package com.example.train_app.dto.request;

import java.io.Serializable;
import java.util.List;



public class ReservationCodeRequestDTO implements Serializable {
    private CustomerDTO customerDTO;


    private List<TicketRequestDTO> ticketRequestDTO;


    public CustomerDTO getCustomerDTO() {
        return customerDTO;
    }

    public void setCustomerDTO(CustomerDTO customerDTO) {
        this.customerDTO = customerDTO;
    }

    public List<TicketRequestDTO> getTicketRequestDTO() {
        return ticketRequestDTO;
    }

    public void setTicketRequestDTO(List<TicketRequestDTO> ticketRequestDTO) {
        this.ticketRequestDTO = ticketRequestDTO;
    }

    public ReservationCodeRequestDTO() {
    }

    public ReservationCodeRequestDTO(CustomerDTO customerDTO, List<TicketRequestDTO> ticketRequestDTO) {
        this.customerDTO = customerDTO;
        this.ticketRequestDTO = ticketRequestDTO;
    }
}
