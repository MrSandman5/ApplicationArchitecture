package com.example.galleryservice.model.project;

import com.example.galleryservice.model.user.Artist;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "artworks")
@Data
@NoArgsConstructor
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "info", nullable = false)
    private String info;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    public Artwork(@NotNull final String name,
                   @NotNull final String info,
                   @NotNull final Artist artist) {
        this.name = name;
        this.info = info;
        this.artist = artist;
    }
}
