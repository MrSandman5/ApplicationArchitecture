package com.example.galleryservice.model.gallery;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class Expo {

    private long id;
    @NotNull
    private String name;
    @NotNull
    private String info;
    @NotNull
    private Long artist;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endTime;
    @Min(value = 0, message = "must be greater than or equal to zero")
    private double ticketPrice;

    @NotNull
    private List<String> artworks;
    @NotNull
    private ExpoStatus status;

    public Expo(@NotNull final String name,
                @NotNull final String info,
                final Long artist,
                @NotNull final LocalDateTime startTime,
                @NotNull final LocalDateTime endTime,
                final double ticketPrice,
                @NotNull final List<String> artworks) {
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

    public boolean isNew() { return status.equals(ExpoStatus.New); }
    public boolean isOpened() { return status.equals(ExpoStatus.Opened); }
    public boolean isClosed() { return status.equals(ExpoStatus.Closed); }

}
