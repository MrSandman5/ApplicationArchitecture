package com.example.galleryservice.project;

import com.example.galleryservice.user.Participant;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Data
public class Expo {

    public enum ExpoStatus{
        NEW,
        STARTED,
        CLOSED
    }

    private final Integer id;
    @NotNull
    private String name;
    @NotNull
    private String info;
    @NotNull
    private final Participant artist;
    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;
    @NotNull
    private List<Artwork> artworks;
    @NotNull
    private ExpoStatus status;

    public Expo(@NotNull final String name,
                @NotNull final String info,
                @NotNull final Participant artist,
                @NotNull final LocalDateTime startTime,
                @NotNull final LocalDateTime endTime,
                @NotNull final List<Artwork> artworks) {
        this.id = -1;
        this.name = name;
        this.info = info;
        this.artist = artist;
        if (startTime.isBefore(endTime)){
            this.startTime = startTime;
            this.endTime = endTime;
        } else if (startTime.isAfter(endTime)){
            this.startTime = endTime;
            this.endTime = startTime;
        }
        this.artworks = artworks;
        this.status = ExpoStatus.NEW;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(@NotNull final LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(@NotNull final LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<Artwork> getArtworks() {
        return artworks;
    }

    public Long getDuration(){
        return ChronoUnit.DAYS.between(startTime, endTime);
    }

    public ExpoStatus getStatus(){
        return status;
    }

    public boolean isNew() {return status.equals(ExpoStatus.NEW);}
    public boolean isStarted() {return status.equals(ExpoStatus.STARTED);}
    public boolean isClosed() {return status.equals(ExpoStatus.CLOSED);}

    public void setStarted(){
        this.status = ExpoStatus.STARTED;
    }

    public void setClosed(){
        this.status = ExpoStatus.CLOSED;
    }
}
