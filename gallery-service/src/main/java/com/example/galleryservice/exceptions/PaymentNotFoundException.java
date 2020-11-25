package com.example.galleryservice.exceptions;


public class PaymentNotFoundException extends Exception {

    public PaymentNotFoundException(final long id){
        super("Payment " + id + " not found");
    }
}
