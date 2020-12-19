package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.configuration.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class ExpoModel {
    private Long expoId;
    private String name;
    private String info;
    private Long artistId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double ticketPrice;
    private Constants.ExpoStatus status;
}
