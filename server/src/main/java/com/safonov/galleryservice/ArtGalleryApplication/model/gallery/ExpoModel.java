package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class ExpoModel {

    @NotBlank(message = "name is mandatory")
    @JsonProperty("name")
    private String name;

    @JsonProperty("info")
    private String info;

    @NotBlank(message = "artist is mandatory")
    @JsonProperty("artist")
    private Long artistId;

    @JsonProperty("startTime")
    private LocalDateTime startTime;

    @JsonProperty("endTime")
    private LocalDateTime endTime;

    @JsonProperty("ticketPrice")
    private Double ticketPrice;
}
