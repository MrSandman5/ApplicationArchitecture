package com.safonov.galleryservice.ArtGalleryApplication.exceptions;

import javax.validation.constraints.NotNull;

public class ReservationAlreadyClosedException extends Exception{
    public ReservationAlreadyClosedException(@NotNull final String message){
        super(message);
    }
}
