package com.safonov.galleryservice.ArtGalleryApplication.exceptions;

import javax.validation.constraints.NotNull;

public class ExpoAlreadyExistedException extends Exception {

    public ExpoAlreadyExistedException(@NotNull final String message){
        super(message);
    }
}
