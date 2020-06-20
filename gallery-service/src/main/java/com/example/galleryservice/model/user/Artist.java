package com.example.galleryservice.model.user;

import com.example.galleryservice.exceptions.*;
import com.example.galleryservice.model.gallery.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Artist extends User {

    @NotNull
    private List<String> artworks = new ArrayList<>();

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
        final Artwork existedArtwork = getStorageDAO().getArtwork(name);
        if (existedArtwork == null) {
            final Artwork newArtwork = new Artwork(name, info, this.getId());
            final long artworkId = getStorageDAO().addArtwork(newArtwork);
            final Artwork addedArtwork = getStorageDAO().getArtwork(artworkId);
            this.artworks.add(addedArtwork.getName());
            return addedArtwork;
        }
        if (existedArtwork.getArtist() != this.getId()) {
            throw new ArtworkNotSimilarArtist("Artwork with name : " + existedArtwork.getName() + " doesn't belong to this artist!");
        }
        if (artworks.contains(existedArtwork.getName())) {
            throw new ArtworkAlreadyExistedException("Artwork with name : " + existedArtwork.getName() + " already added to expo list!");
        }
        this.artworks.add(existedArtwork.getName());
        getStorageDAO().updateArtist(this);
        return existedArtwork;
    }

    @SneakyThrows
    public Artwork addArtwork(@NotNull final Artwork artwork){
        this.checkAuthentication();
        if (artwork.getArtist() != this.getId()) {
            throw new ArtworkNotSimilarArtist("Artwork with name : " + artwork.getName() + " doesn't belong to this artist!");
        }
        if (artworks.contains(artwork.getName())) {
            throw new ArtworkAlreadyExistedException("Artwork with name : " + artwork.getName() + " already added to expo list!");
        }
        final Artwork existedArtwork = getStorageDAO().getArtwork(artwork.getName());
        if (existedArtwork == null) {
            final long artworkId = getStorageDAO().addArtwork(artwork);
            final Artwork addedArtwork = getStorageDAO().getArtwork(artworkId);
            this.artworks.add(addedArtwork.getName());
            return addedArtwork;
        }
        this.artworks.add(existedArtwork.getName());
        getStorageDAO().updateArtist(this);
        return existedArtwork;
    }

    @SneakyThrows
    public double acceptRoyalties(@NotNull final String expo){
        this.checkAuthentication();
        final Expo closedExpo = getStorageDAO().getExpo(expo);
        if (closedExpo == null){
            throw new ExpoNotFoundException(expo);
        } if (!closedExpo.isClosed()){
            throw new ExpoHasNotClosedException("Expo with id : " + closedExpo.getId() + " hasn't closedQ");
        }
        final OwnerArtistPayment payment = getStorageDAO().getOwnerArtistPayment(closedExpo.getId());
        return payment.getPrice();
    }

}
