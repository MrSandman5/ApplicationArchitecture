package com.safonov.galleryservice.ArtGalleryApplication.entity.actor;

import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.ClientOwnerPayment;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Expo;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.OwnerArtistPayment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "owner")
@EqualsAndHashCode(callSuper = true)
public class Owner extends User {

    @OneToMany(mappedBy = "expo_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expo> expos;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClientOwnerPayment> clientOwnerPayments;

    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OwnerArtistPayment> ownerArtistPayments;

    public Owner(@NotNull final String firstName,
                  @NotNull final String secondName) {
        this.firstName = firstName;
        this.lastName = secondName;
    }
}
