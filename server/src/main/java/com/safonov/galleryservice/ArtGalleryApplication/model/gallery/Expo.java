package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.model.user.Artist;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "expo")
@Data
@NoArgsConstructor
public class Expo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "info")
    private String info;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @Column(name = "artist")
    private Artist artist;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "startTime")
    private LocalDateTime startTime;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "endTime")
    private LocalDateTime endTime;

    @Min(value = 0, message = "must be greater than or equal to zero")
    @Column(name = "ticketPrice")
    private Double ticketPrice;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status")
    private ExpoStatus status;

    @OneToMany(mappedBy = "artwork", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private Set<Artwork> artworks;

    public Expo(@NotNull final String name,
                @NotNull final String info,
                @NotNull final Artist artist,
                @NotNull final LocalDateTime startTime,
                @NotNull final LocalDateTime endTime,
                final Double ticketPrice,
                @NotNull final Set<Artwork> artworks) {
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
        this.id = expo.id;
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
