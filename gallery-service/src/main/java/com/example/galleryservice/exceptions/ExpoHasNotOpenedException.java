package com.example.galleryservice.exceptions;

import javax.validation.constraints.NotNull;

public class ExpoHasNotOpenedException extends Exception {

    public ExpoHasNotOpenedException(@NotNull final String message){
        super(message);
    }
}
