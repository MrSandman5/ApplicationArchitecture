package com.example.galleryservice.model.gallery;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class Artwork {

    private long id;
    private String name;
    private String info;
    private Long artist;

    public Artwork(@NotNull final String name,
                   @NotNull final String info,
                   final Long artist) {
        this.name = name;
        this.info = info;
        this.artist = artist;
    }
}
