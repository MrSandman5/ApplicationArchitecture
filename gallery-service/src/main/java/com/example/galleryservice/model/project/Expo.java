package com.example.galleryservice.model.project;

import com.example.galleryservice.model.user.Artist;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Table(name = "expos")
@Data
@NoArgsConstructor
public class Expo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "info", nullable = false)
    private String info;

    @ManyToOne(optional = false)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "startTime")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "endTime")
    private LocalDateTime endTime;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "artworks")
    private List<Artwork> artworks;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ExpoStatus status;

    public Expo(@NotNull final String name,
                @NotNull final String info,
                @NotNull final Artist artist,
                @NotNull final LocalDateTime startTime,
                @NotNull final LocalDateTime endTime,
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
        this.artworks = artworks;
        this.status = ExpoStatus.NEW;
    }

    public Long getDuration(){
        return ChronoUnit.DAYS.between(startTime, endTime);
    }

    public boolean isNew() {return status.equals(ExpoStatus.NEW);}
    public boolean isStarted() {return status.equals(ExpoStatus.STARTED);}
    public boolean isClosed() {return status.equals(ExpoStatus.CLOSED);}

}
