package com.example.galleryservice.exceptions;

import javax.validation.constraints.NotNull;

public class NotSameExpoException extends Exception {
    public NotSameExpoException(@NotNull final String message) {
        super(message);
    }
}
