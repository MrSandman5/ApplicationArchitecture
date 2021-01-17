package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class ArtworkModel {
    @NotBlank(message = "name is mandatory")
    @JsonProperty("name")
    private String name;

    @JsonProperty("info")
    private String info;
}
