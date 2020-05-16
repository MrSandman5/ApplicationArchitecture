package com.example.galleryservice.exceptions;

import javax.validation.constraints.NotNull;

public class NoSuchMoneyOnAccountException extends Exception {
    public NoSuchMoneyOnAccountException(@NotNull final String message){
        super(message);
    }
}
