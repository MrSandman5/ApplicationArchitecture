package com.safonov.galleryservice.ArtGalleryApplication.model.info;

import com.safonov.galleryservice.ArtGalleryApplication.configuration.Constants;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Ticket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class ReservationModel {
    private Long reservationId;
    private Constants.ReservationStatus status;
    private List<Ticket> tickets;
}
