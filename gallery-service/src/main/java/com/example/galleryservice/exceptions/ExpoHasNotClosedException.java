package com.example.galleryservice.exceptions;

import javax.validation.constraints.NotNull;

public class ExpoHasNotClosedException extends Exception {

    public ExpoHasNotClosedException(@NotNull final String message){
        super(message);
    }
}
