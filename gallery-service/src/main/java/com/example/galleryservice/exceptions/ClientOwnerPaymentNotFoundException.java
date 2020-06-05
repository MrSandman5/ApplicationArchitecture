package com.example.galleryservice.exceptions;

import javax.validation.constraints.NotNull;

public class ClientOwnerPaymentNotFoundException extends Exception {

    public ClientOwnerPaymentNotFoundException(@NotNull final String message){
        super(message);
    }
}
