package com.example.galleryservice.view;

import javax.validation.constraints.NotNull;

public class PaymentReservationForm {

    private long reservation;
    @NotNull
    private String owner;

    public long getReservation() {
        return reservation;
    }

    public void setReservation(final long reservation) {
        this.reservation = reservation;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(@NotNull final  String owner) {
        this.owner = owner;
    }
}
