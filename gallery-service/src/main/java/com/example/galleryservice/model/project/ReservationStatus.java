package com.example.galleryservice.model.project;

import javax.validation.constraints.NotNull;

public enum ReservationStatus {

    New("New"), Payed("Payed"), Closed("Closed");

    ReservationStatus(@NotNull final String status){ }
}
