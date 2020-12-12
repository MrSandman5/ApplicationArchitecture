package com.safonov.galleryservice.ArtGalleryApplication.exceptions;

import javax.validation.constraints.NotNull;

public class ExpoHasNotOpenedException extends Exception {

    public ExpoHasNotOpenedException(@NotNull final String message){
        super(message);
    }
}
