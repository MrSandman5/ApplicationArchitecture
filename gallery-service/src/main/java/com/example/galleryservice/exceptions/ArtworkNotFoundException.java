package com.example.galleryservice.exceptions;

import javax.validation.constraints.NotNull;

public class ArtworkNotFoundException extends Exception {

    public ArtworkNotFoundException(@NotNull final String name){
        super("Artwork " + name + " not found");
    }
}
