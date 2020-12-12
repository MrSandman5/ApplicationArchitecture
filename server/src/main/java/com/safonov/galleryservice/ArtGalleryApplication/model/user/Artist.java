package com.safonov.galleryservice.ArtGalleryApplication.model.user;

import com.safonov.galleryservice.ArtGalleryApplication.exceptions.ArtworkAlreadyExistedException;
import com.safonov.galleryservice.ArtGalleryApplication.exceptions.ArtworkNotSimilarArtist;
import com.safonov.galleryservice.ArtGalleryApplication.exceptions.ExpoHasNotClosedException;
import com.safonov.galleryservice.ArtGalleryApplication.exceptions.ExpoNotFoundException;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.Artwork;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.Expo;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.OwnerArtistPayment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Artist extends User {

    @OneToMany(mappedBy = "artwork", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private final List<Artwork> artworks = new ArrayList<>();

    @OneToMany(mappedBy = "owner_artist_payment", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private final List<OwnerArtistPayment> ownerArtistPayments = new ArrayList<>();

    public Artist(@NotNull final String login,
                  @NotNull final String password,
                  @NotNull final String name,
                  @NotNull final String email) {
        super(login, password, name, email);
    }

    public Artist(@NotNull final User user) {
        super(user);
    }

    @SneakyThrows
    public Artwork addArtwork(@NotNull final String name,
                              @NotNull final String info){
        this.checkAuthentication();
        final Artwork existedArtwork = getStorageRepository().getArtwork(name);
        if (existedArtwork == null) {
            final Artwork newArtwork = new Artwork(name, info, this);
            final long artworkId = getStorageRepository().addArtwork(newArtwork).getId();
            final Artwork addedArtwork = getStorageRepository().getArtwork(artworkId);
            this.artworks.add(addedArtwork);
            return addedArtwork;
        }
        if (!existedArtwork.getArtist().getId().equals(this.getId())) {
            throw new ArtworkNotSimilarArtist("Artwork with name : " + existedArtwork.getName() + " doesn't belong to this artist!");
        }
        if (artworks.contains(existedArtwork)) {
            throw new ArtworkAlreadyExistedException("Artwork with name : " + existedArtwork.getName() + " already added to expo list!");
        }
        this.artworks.add(existedArtwork);
        getStorageRepository().updateArtist(this);
        return existedArtwork;
    }

    @SneakyThrows
    public Artwork addArtwork(@NotNull final Artwork artwork){
        this.checkAuthentication();
        if (!artwork.getArtist().getId().equals(this.getId())) {
            throw new ArtworkNotSimilarArtist("Artwork with name : " + artwork.getName() + " doesn't belong to this artist!");
        }
        if (artworks.contains(artwork)) {
            throw new ArtworkAlreadyExistedException("Artwork with name : " + artwork.getName() + " already added to expo list!");
        }
        final Artwork existedArtwork = getStorageRepository().getArtwork(artwork.getName());
        if (existedArtwork == null) {
            final long artworkId = getStorageRepository().addArtwork(artwork).getId();
            final Artwork addedArtwork = getStorageRepository().getArtwork(artworkId);
            this.artworks.add(addedArtwork);
            return addedArtwork;
        }
        this.artworks.add(existedArtwork);
        getStorageRepository().updateArtist(this);
        return existedArtwork;
    }

    @SneakyThrows
    public double acceptRoyalties(@NotNull final String expo){
        this.checkAuthentication();
        final Expo closedExpo = getStorageRepository().getExpo(expo);
        if (closedExpo == null){
            throw new ExpoNotFoundException(expo);
        } if (!closedExpo.isClosed()){
            throw new ExpoHasNotClosedException("Expo with id : " + closedExpo.getId() + " hasn't closedQ");
        }
        final OwnerArtistPayment payment = getStorageRepository().getOwnerArtistPayment(closedExpo.getId());
        return payment.getPrice();
    }
}
