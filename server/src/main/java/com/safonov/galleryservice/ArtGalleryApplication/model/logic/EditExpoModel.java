package com.safonov.galleryservice.ArtGalleryApplication.model.logic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class EditExpoModel {

    @NotNull
    @JsonProperty("expo")
    private Long expo;

    @NotBlank(message = "settings is mandatory")
    @JsonProperty("settings")
    private String settings;

    @NotBlank(message = "data is mandatory")
    @JsonProperty("data")
    private String data;
}
