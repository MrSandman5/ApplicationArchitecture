package com.safonov.galleryservice.ArtGalleryApplication.exceptions;

import javax.validation.constraints.NotNull;

public class ExpoAlreadyClosedException extends Exception {
    public ExpoAlreadyClosedException(@NotNull final String message){
        super(message);
    }
}
