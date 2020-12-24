package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.safonov.galleryservice.ArtGalleryApplication.model.info.ReservationModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class PayForReservationModel {
    @JsonProperty("owner")
    private Long ownerId;

    private ReservationModel reservation;
}

