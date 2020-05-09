package com.example.galleryservice.exceptions;

public class DBConnectionException extends Exception{
    public DBConnectionException() {
        super("Could not connect to database");
    }
}
