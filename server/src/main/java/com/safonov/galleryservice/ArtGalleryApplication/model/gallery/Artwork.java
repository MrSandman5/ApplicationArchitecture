package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.model.user.Artist;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "artwork")
@Data
@NoArgsConstructor
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "info")
    private String info;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Artist artist;

    public Artwork(@NotNull final String name,
                   @NotNull final String info,
                   @NotNull final Artist artist) {
        this.name = name;
        this.info = info;
        this.artist = artist;
    }
}