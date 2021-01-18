package com.safonov.galleryservice.ArtGalleryApplication.model.logic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PayForReservationModel {

    @JsonProperty("owner")
    private Long owner;

    @JsonProperty("reservation")
    private Long reservation;
}

