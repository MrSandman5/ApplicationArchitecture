package com.example.galleryservice.model.gallery;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Data
@NoArgsConstructor
public class Expo {

    private long id;
    private String name;
    private String info;
    private Long artist;

    @JsonFormat(pattern = "dd/MM/yyyy hh:mm")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "dd/MM/yyyy hh:mm")
    private LocalDateTime endTime;
    private double ticketPrice;

    private List<Artwork> artworks;
    private ExpoStatus status;

    public Expo(@NotNull final String name,
                @NotNull final String info,
                final Long artist,
                @NotNull final LocalDateTime startTime,
                @NotNull final LocalDateTime endTime,
                final double ticketPrice,
                @NotNull final List<Artwork> artworks) {
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
        this.ticketPrice = ticketPrice;
        this.artworks = artworks;
        this.status = ExpoStatus.New;
    }

    public Expo(@NotNull final Expo expo){
        this.name = expo.name;
        this.info = expo.info;
        this.artist = expo.artist;
        this.startTime = expo.startTime;
        this.endTime = expo.endTime;
        this.ticketPrice = expo.ticketPrice;
        this.artworks = expo.artworks;
        this.status = expo.status;
    }

    public Long getDuration(){
        return ChronoUnit.DAYS.between(startTime, endTime);
    }

    public boolean isNew() { return status.equals(ExpoStatus.New); }
    public boolean isOpened() { return status.equals(ExpoStatus.Opened); }
    public boolean isClosed() { return status.equals(ExpoStatus.Closed); }

}
