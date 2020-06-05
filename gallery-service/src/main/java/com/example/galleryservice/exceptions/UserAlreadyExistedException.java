package com.example.galleryservice.exceptions;

import javax.validation.constraints.NotNull;

public class UserAlreadyExistedException extends Exception {

    public UserAlreadyExistedException(@NotNull final String login){
        super("User " + login + " already exists");
    }
}
