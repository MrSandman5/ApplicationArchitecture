package com.example.galleryservice.exceptions;

public class ReservationNotPaidException extends Exception {

    public ReservationNotPaidException(final long id){
        super("Reservation with id : " + id + " not paid!");
    }
}
