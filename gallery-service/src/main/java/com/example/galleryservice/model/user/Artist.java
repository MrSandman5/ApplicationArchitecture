package com.example.galleryservice.model.user;

import com.example.galleryservice.data.StorageDAO;
import com.example.galleryservice.exceptions.*;
import com.example.galleryservice.model.project.*;
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

    private final List<Artwork> artworks = new ArrayList<>();

    public Artist(@NotNull final String login,
                  @NotNull final String password,
                  @NotNull final String name,
                  @NotNull final String email) {
        super(login, password, name, email, UserRole.Artist);
    }

    public Artist(@NotNull final User user) {
        super(user);
    }

    @SneakyThrows
    public Artwork addArtwork(@NotNull final Artwork artwork){
        this.checkAuthentication();
        if (artworks.contains(artwork))
            throw new ArtworkAlreadyExistedException("Artwork with id : " + artwork.getId() + " already existed for this user!");
        this.artworks.add(artwork);
        getStorageDAO().addArtwork(artwork);
        return artwork;
    }

    @SneakyThrows
    public double acceptRoyalties(@NotNull final Expo expo){
        this.checkAuthentication();
        final Expo closedExpo = getStorageDAO().getExpo(expo.getId());
        if (closedExpo == null){
            throw new ExpoNotFoundException(expo.getId());
        } if (!closedExpo.isClosed()){
            throw new ExpoHasNotClosedException("Expo with id : " + closedExpo.getId() + " hasn't closedQ");
        }
        final OwnerArtistPayment payment = getStorageDAO().getOwnerArtistPayment(closedExpo.getId());
        return payment.getAmount();
    }

}
