package com.safonov.galleryservice.ArtGalleryApplication.exceptions;

public class IncorrectPasswordException extends Exception{
    public IncorrectPasswordException() {
        super("Incorrect login or password");
    }
}
