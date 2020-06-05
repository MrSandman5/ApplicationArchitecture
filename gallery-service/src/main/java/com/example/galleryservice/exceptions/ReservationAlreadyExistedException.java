package com.example.galleryservice.exceptions;

import javax.validation.constraints.NotNull;

public class ReservationAlreadyExistedException extends Exception {

    public ReservationAlreadyExistedException(@NotNull final String message){
        super(message);
    }
}
