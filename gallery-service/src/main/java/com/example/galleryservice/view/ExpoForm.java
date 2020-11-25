package com.example.galleryservice.view;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ExpoForm {

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

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
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

    public Long getArtist() {
        return artist;
    }

    public void setArtist(@NotNull final Long artist) {
        this.artist = artist;
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

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(@NotNull final double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}
