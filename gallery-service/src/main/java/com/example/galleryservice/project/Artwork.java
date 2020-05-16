package com.example.galleryservice.project;

import com.example.galleryservice.user.Participant;

import javax.validation.constraints.NotNull;

public class Artwork {

    private final Integer id;
    @NotNull
    private String name;
    @NotNull
    private String info;
    @NotNull
    private final Participant artist;

    public Artwork(final Integer id,
                   @NotNull final String name,
                   @NotNull final String info,
                   @NotNull final Participant artist) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.artist = artist;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(final String info) {
        this.info = info;
    }

    public Participant getArtist() {
        return artist;
    }
}
