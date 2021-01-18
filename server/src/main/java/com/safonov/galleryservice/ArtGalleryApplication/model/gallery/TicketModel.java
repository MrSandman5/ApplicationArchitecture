package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketModel {

    @JsonProperty("reservation")
    private Long reservation;

    @JsonProperty("expo")
    private Long expo;

    @JsonProperty("artist")
    private Double cost;
}
