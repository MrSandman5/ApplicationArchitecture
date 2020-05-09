package com.example.galleryservice.project;

import com.example.galleryservice.user.Participant;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Data
public class Expo {

    private final Integer id;
    @NotNull
    private String name;
    @NotNull
    private String info;
    @NotNull
    private final Participant artist;
    @NotNull
    private LocalDate beginTime;
    @NotNull
    private LocalDate endTime;
    @NotNull
    private List<Artwork> artworks;

    public Expo(final Integer id,
                @NotNull final String name,
                @NotNull final String info,
                @NotNull final Participant artist,
                @NotNull final LocalDate beginTime,
                @NotNull final LocalDate endTime,
                @NotNull final List<Artwork> artworks) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.artist = artist;
        if (beginTime.isBefore(endTime)){
            this.beginTime = beginTime;
            this.endTime = endTime;
        } else if (beginTime.isAfter(endTime)){
            this.beginTime = endTime;
            this.endTime = beginTime;
        }
        this.artworks = artworks;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull final String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(@NotNull final String info) {
        this.info = info;
    }

    public Participant getArtist() {
        return artist;
    }

    public LocalDate getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(@NotNull final LocalDate beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(@NotNull final LocalDate endTime) {
        this.endTime = endTime;
    }

    public List<Artwork> getArtworks() {
        return artworks;
    }

    public void setArtworks(@NotNull final List<Artwork> artworks) {
        this.artworks = artworks;
    }

    public Long getDuration(){
        return ChronoUnit.DAYS.between(beginTime, endTime);
    }
}
