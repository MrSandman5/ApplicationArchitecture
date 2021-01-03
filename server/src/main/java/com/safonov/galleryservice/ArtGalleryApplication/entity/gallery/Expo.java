package com.safonov.galleryservice.ArtGalleryApplication.entity.gallery;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.safonov.galleryservice.ArtGalleryApplication.configuration.Constants;
import com.safonov.galleryservice.ArtGalleryApplication.entity.AbstractEntity;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Artist;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "expo")
@EqualsAndHashCode(callSuper = true)
public class Expo extends AbstractEntity {

    @Column(unique = true, name = "name", nullable = false)
    private String name;

    @Column(name = "info")
    private String info;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "startTime", nullable = false)
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "endTime", nullable = false)
    private LocalDateTime endTime;

    @Min(value = 0, message = "must be greater than or equal to zero")
    @Column(name = "ticketPrice", nullable = false)
    private Double ticketPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Constants.ExpoStatus status;

    @JsonBackReference
    @OneToMany(mappedBy = "expo", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Artwork> artworks = new HashSet<>();

    public Expo(@NotNull final String name,
                @NotNull final String info,
                @NotNull final Artist artist,
                @NotNull final LocalDateTime startTime,
                @NotNull final LocalDateTime endTime,
                final Double ticketPrice) {
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
        this.status = Constants.ExpoStatus.New;
    }

    public boolean isNew() { return status.equals(Constants.ExpoStatus.New); }
    public boolean isOpened() { return status.equals(Constants.ExpoStatus.Opened); }
    public boolean isClosed() { return status.equals(Constants.ExpoStatus.Closed); }
}
