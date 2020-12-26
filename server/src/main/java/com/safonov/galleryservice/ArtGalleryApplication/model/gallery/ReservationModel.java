package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Ticket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class ReservationModel {

    @NotBlank(message = "id is mandatory")
    @JsonProperty("id")
    private Long reservationId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("tickets")
    private List<Ticket> tickets;
}
