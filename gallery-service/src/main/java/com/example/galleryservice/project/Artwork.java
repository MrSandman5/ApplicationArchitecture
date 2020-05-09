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
    private Integer cost;

    public Artwork(final Integer id,
                   @NotNull final String name,
                   @NotNull final String info,
                   @NotNull final Participant artist,
                   final Integer cost) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.artist = artist;
        this.cost = cost;
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

    public Integer getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
