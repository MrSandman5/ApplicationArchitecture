package com.safonov.galleryservice.ArtGalleryApplication.model.logic;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.ExpoModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class EditExpoModel {

    @JsonProperty("expo")
    private ExpoModel expo;

    @NotBlank(message = "settings is mandatory")
    @JsonProperty("settings")
    private String settings;

    @NotBlank(message = "data is mandatory")
    @JsonProperty("data")
    private String data;
}
