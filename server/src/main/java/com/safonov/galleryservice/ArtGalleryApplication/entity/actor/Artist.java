package com.safonov.galleryservice.ArtGalleryApplication.entity.actor;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Artwork;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.OwnerArtistPayment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "artist")
@EqualsAndHashCode(callSuper = true)
public class Artist extends User {

    @JsonBackReference
    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Artwork> artworks = new HashSet<>();

    @JsonBackReference
    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<OwnerArtistPayment> ownerArtistPayments = new HashSet<>();

    public Artist(@NotNull final String firstName,
                  @NotNull final String secondName) {
        this.firstName = firstName;
        this.lastName = secondName;
    }
}
