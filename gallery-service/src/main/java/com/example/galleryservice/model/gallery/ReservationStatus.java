package com.example.galleryservice.model.gallery;

import javax.validation.constraints.NotNull;

public enum ReservationStatus {

    New("New"), Payed("Payed"), Closed("Closed");

    ReservationStatus(@NotNull final String status){ }
}
