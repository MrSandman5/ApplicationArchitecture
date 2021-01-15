package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Ticket;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class ReservationModel {

    @NotNull
    @JsonProperty("id")
    private Long reservationId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("tickets")
    private List<TicketModel> tickets;
}
