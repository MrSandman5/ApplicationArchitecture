package com.safonov.galleryservice.ArtGalleryApplication.exceptions;

import javax.validation.constraints.NotNull;

public class ExpoNotFoundException extends Exception {

    public ExpoNotFoundException(final long id){
        super("Expo with id : " + id + " not found");
    }

    public ExpoNotFoundException(@NotNull final String name){
        super("Expo with name : " + name + " not found");
    }
}
