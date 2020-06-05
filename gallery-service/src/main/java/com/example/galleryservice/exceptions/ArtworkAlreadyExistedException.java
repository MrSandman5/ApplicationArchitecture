package com.example.galleryservice.exceptions;

import javax.validation.constraints.NotNull;

public class ArtworkAlreadyExistedException extends Exception {

    public ArtworkAlreadyExistedException(@NotNull final String message){
        super(message);
    }
}
