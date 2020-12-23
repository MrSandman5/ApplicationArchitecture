package com.safonov.galleryservice.ArtGalleryApplication.entity.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Artist;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Client;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Owner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@Table(name = "owner_artist_payment")
@EqualsAndHashCode(callSuper = true)
public class OwnerArtistPayment extends Payment{

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "expo_id", nullable = false)
    private Expo expo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    public OwnerArtistPayment(@NotNull final Expo expo,
                              @NotNull final Owner owner,
                              @NotNull final Artist artist,
                              final Double price) {
        super(price);
        this.expo = expo;
        this.artist = artist;
        this.owner = owner;
    }
}
