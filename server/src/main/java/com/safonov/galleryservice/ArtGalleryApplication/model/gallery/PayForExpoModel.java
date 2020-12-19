package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class PayForExpoModel {
    private Long ownerId;
    private ExpoModel expo;
}
