package com.example.galleryservice.exceptions;

import javax.validation.constraints.NotNull;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(@NotNull final String login){
        super("User " + login + " not found");
    }
}
