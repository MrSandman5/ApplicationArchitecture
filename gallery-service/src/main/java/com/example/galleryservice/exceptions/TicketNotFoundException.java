package com.example.galleryservice.exceptions;

public class TicketNotFoundException extends Exception {

    public TicketNotFoundException(final long id){
        super("Ticket with id : " + id + " not found");
    }
}
