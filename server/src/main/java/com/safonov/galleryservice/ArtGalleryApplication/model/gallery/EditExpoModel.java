package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.configuration.Constants;
import com.safonov.galleryservice.ArtGalleryApplication.model.info.ExpoModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class EditExpoModel {
    private ExpoModel expo;
    private Constants.EditSettings settings;
    private String data;
}
