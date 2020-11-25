package com.example.galleryservice.exceptions;

import javax.validation.constraints.NotNull;

public class ArtworkNotSimilarArtist extends Exception {

    public ArtworkNotSimilarArtist(@NotNull final String message){
        super(message);
    }
}
