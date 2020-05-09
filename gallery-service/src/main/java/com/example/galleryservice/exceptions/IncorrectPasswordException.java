package com.example.galleryservice.exceptions;

public class IncorrectPasswordException extends Exception{
    public IncorrectPasswordException() {
        super("Incorrect login or password");
    }
}
