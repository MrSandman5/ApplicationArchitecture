package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.configuration.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class EditExpoModel {
    private Long ownerId;
    private ExpoModel expo;
    private Constants.EditSettings settings;
    private String data;
}
