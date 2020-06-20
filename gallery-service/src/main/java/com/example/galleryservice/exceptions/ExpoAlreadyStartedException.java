package com.example.galleryservice.exceptions;

import javax.validation.constraints.NotNull;

public class ExpoAlreadyStartedException extends Exception {

    public ExpoAlreadyStartedException(@NotNull final String message){
        super(message);
    }
}
