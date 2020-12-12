package com.safonov.galleryservice.ArtGalleryApplication.exceptions;

import javax.validation.constraints.NotNull;

public class NotAuthenticatedException extends Exception{
    public NotAuthenticatedException(@NotNull final String message){
        super(message);
    }
}
