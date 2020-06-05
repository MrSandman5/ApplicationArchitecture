package com.example.galleryservice.exceptions;

import javax.validation.constraints.NotNull;

public class EndBeforeStartException extends Exception{
    public EndBeforeStartException(@NotNull final String message) {
        super(message);
    }
}
