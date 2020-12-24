package com.safonov.galleryservice.ArtGalleryApplication.model.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class ExpoModel {

    @JsonProperty("name")
    private String name;

    @JsonProperty("info")
    private String info;

    @JsonProperty("artist")
    private Long artistId;

    @JsonProperty("startTime")
    private LocalDateTime startTime;

    @JsonProperty("endTime")
    private LocalDateTime endTime;

    @JsonProperty("ticketPrice")
    private Double ticketPrice;
}
