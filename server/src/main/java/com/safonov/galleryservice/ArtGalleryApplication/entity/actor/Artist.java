package com.safonov.galleryservice.ArtGalleryApplication.entity.actor;

import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Artwork;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.OwnerArtistPayment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "artist")
@EqualsAndHashCode(callSuper = true)
public class Artist extends User {

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Artwork> artworks;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OwnerArtistPayment> ownerArtistPayments;

    public Artist(@NotNull final String firstName,
                 @NotNull final String secondName) {
        this.firstName = firstName;
        this.lastName = secondName;
    }
}
