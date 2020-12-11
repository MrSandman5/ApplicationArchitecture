package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.model.user.Artist;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Owner;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "owner_artist_payment")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OwnerArtistPayment extends Payment {

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @NotNull
    @Column(name = "expo")
    private Expo expo;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @Column(name = "owner")
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @Column(name = "artist")
    private Artist artist;

    public OwnerArtistPayment(@NotNull final Expo expo,
                              @NotNull final Owner owner,
                              @NotNull final Artist artist,
                              final double amount) {
        super(amount);
        this.expo = expo;
        this.owner = owner;
        this.artist = artist;
    }
}
