package com.example.galleryservice.exceptions;

import javax.validation.constraints.NotNull;

public class IncorrectRoleException extends Exception{

    public IncorrectRoleException(@NotNull final String login){
        super("User " + login + " doesn't have same role");
    }
}
