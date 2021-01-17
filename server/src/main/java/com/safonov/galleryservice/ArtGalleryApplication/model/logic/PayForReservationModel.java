package com.safonov.galleryservice.ArtGalleryApplication.model.logic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class PayForReservationModel {
    @NotNull
    @JsonProperty("owner")
    private Long ownerId;

    @JsonProperty("reservation")
    private Long reservation;
}

