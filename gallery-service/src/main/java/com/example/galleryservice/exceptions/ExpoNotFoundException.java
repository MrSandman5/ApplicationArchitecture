package com.example.galleryservice.exceptions;

public class ExpoNotFoundException extends Exception {

    public ExpoNotFoundException(final long id){
        super("Expo with id : " + id + " not found");
    }
}
