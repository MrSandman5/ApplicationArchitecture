package com.example.galleryservice.exceptions;

public class ReservationNotFoundException extends Exception {

    public ReservationNotFoundException(final long id){
        super("Reservation with id : " + id + " not found");
    }
}
