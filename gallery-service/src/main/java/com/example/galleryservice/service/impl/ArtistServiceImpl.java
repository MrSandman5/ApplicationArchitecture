package com.example.galleryservice.service.impl;

import com.example.galleryservice.exceptions.IncorrectPasswordException;
import com.example.galleryservice.model.project.Artwork;
import com.example.galleryservice.model.project.Expo;
import com.example.galleryservice.model.user.Artist;
import com.example.galleryservice.model.user.Client;
import com.example.galleryservice.model.user.Owner;
import com.example.galleryservice.repository.ArtistRepository;
import com.example.galleryservice.repository.ArtworkRepository;
import com.example.galleryservice.repository.ExpoRepository;
import com.example.galleryservice.service.ArtistService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Slf4j
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtworkRepository artworkRepository;
    private final ExpoRepository expoRepository;
    private final OwnerServiceImpl ownerService;

    @Autowired
    public ArtistServiceImpl(@NotNull final ArtistRepository artistRepository,
                             @NotNull final ArtworkRepository artworkRepository,
                             @NotNull final ExpoRepository expoRepository,
                             @NotNull final OwnerServiceImpl ownerService) {
        this.artistRepository = artistRepository;
        this.artworkRepository = artworkRepository;
        this.expoRepository = expoRepository;
        this.ownerService = ownerService;
    }

    @Override
    public void register(@NotNull final String login,
                         @NotNull final String password,
                         @NotNull final String name,
                         @NotNull final String email) {
        Artist registeredArtist = artistRepository.save(
                new Artist(login, password, name, email));
        log.info("IN register<Artist> - artist: {} successfully registered", registeredArtist);
    }

    @Override
    public void register(@NotNull final Artist user) {
        Artist registeredArtist = artistRepository.save(user);
        log.info("IN register<Artist> - artist: {} successfully registered", registeredArtist);
    }

    @SneakyThrows
    @Override
    public void signIn(@NotNull final String login, @NotNull final String password) {
        Artist artist = this.findUserByLogin(login);
        if (artist.isAuthenticated()){
            return;
        }
        if (!password.equals(artist.getPassword())){
            throw new IncorrectPasswordException();
        }
        artist.setAuthentication(true);
        log.info("IN authenticate - artist: {} successful signIn", artist);
    }

    @Override
    public void signOut(@NotNull final String login) {
        Artist artist = this.findUserByLogin(login);
        if (!artist.isAuthenticated()){
            return;
        }
        artist.setAuthentication(false);
        log.info("IN authenticate - artist: {} successful signOut", artist);
    }

    @Override
    public List<Artist> getAllUsers() {
        List<Artist> result = artistRepository.findAll();
        log.info("IN getAllUsers<Artist> - {} artists found", result.size());
        return result;
    }

    @Override
    public Artist findUserByLogin(@NotNull final String login) {
        Artist result = artistRepository.findUserByLogin(login);
        log.info("IN findUserByLogin<Artist> - artist: found by login: {}", login);
        return result;
    }

    @Override
    public Artist findUserByEmail(@NotNull final String email) {
        Artist result = artistRepository.findUserByEmail(email);
        log.info("IN findUserByEmail<Artist> - artist: found by email: {}", email);
        return result;
    }

    @Override
    public Artist findUserById(final Long id) {
        Artist result = artistRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findUserById<Artist> - no artist found by id: {}", id);
            return null;
        }

        log.info("IN findUserById<Artist> - artist: {} found by id: {}", result, id);
        return result;
    }

    @Override
    public void deleteUser(final Long id) {
        artistRepository.deleteById(id);
        log.info("IN deleteUser<Artist> - artist with id: {} successfully deleted", id);
    }

    @SneakyThrows
    public Artwork addArtwork(@NotNull final Artist artist,
                              @NotNull final Artwork artwork){
        artist.checkAuthentication();
        final Artwork addedArtwork = artworkRepository.findById(artwork.getId()).orElse(null);
        if (addedArtwork == null){
            artworkRepository.save(artwork);
            log.info("IN addArtwork - artwork : {} successfully added", artwork);
        }
        artist.getArtworks().add(addedArtwork);
        log.info("IN addArtwork - artwork : {} successfully added to expo list", artwork);
        return addedArtwork;
    }

    @SneakyThrows
    public Double acceptRoyalties(@NotNull final Artist artist,
                                  @NotNull final Owner owner,
                                  @NotNull final Expo expo){
        artist.checkAuthentication();
        final Double royalties = ownerService.payForExpo(owner, expo);
        log.warn("IN acceptRoyalties - payment: {} for expo {} was accepted", royalties, expo);
        return royalties;
    }
}
