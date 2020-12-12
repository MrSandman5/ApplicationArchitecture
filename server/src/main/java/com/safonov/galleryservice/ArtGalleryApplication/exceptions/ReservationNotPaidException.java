package com.safonov.galleryservice.ArtGalleryApplication.exceptions;

public class ReservationNotPaidException extends Exception {

    public ReservationNotPaidException(final long id){
        super("Reservation with id : " + id + " not paid!");
    }
}
