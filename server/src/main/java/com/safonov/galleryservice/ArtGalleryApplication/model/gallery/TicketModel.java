package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TicketModel {

    @JsonProperty("reservation")
    private Long reservationId;

    @JsonProperty("expo")
    private Long expoId;

    @JsonProperty("artist")
    private Double cost;
}
