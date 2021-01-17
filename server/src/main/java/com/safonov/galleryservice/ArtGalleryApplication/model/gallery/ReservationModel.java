package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationModel {

    @NotNull
    @JsonProperty("id")
    private Long reservationId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("cost")
    private Double cost;

    /*@JsonProperty("tickets")
    private List<Ticket> tickets;*/
}
