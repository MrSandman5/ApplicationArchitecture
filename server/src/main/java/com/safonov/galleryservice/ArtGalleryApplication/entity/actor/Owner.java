package com.safonov.galleryservice.ArtGalleryApplication.entity.actor;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.ClientOwnerPayment;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.OwnerArtistPayment;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "owner")
public class Owner extends User {

    @JsonBackReference
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ClientOwnerPayment> clientOwnerPayments = new HashSet<>();

    @JsonBackReference
    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<OwnerArtistPayment> ownerArtistPayments = new HashSet<>();

    public Owner(@NotNull final String firstName,
                 @NotNull final String secondName) {
        this.firstName = firstName;
        this.lastName = secondName;
    }
}
