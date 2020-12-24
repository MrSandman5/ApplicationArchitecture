package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.safonov.galleryservice.ArtGalleryApplication.model.info.ExpoModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class EditExpoModel {
    private ExpoModel expo;
    @JsonProperty("settings")
    private String settings;
    @JsonProperty("data")
    private String data;
}
