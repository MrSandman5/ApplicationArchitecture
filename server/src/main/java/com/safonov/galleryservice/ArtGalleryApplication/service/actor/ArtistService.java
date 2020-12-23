package com.safonov.galleryservice.ArtGalleryApplication.service.actor;

import com.safonov.galleryservice.ArtGalleryApplication.data.actor.ArtistRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.gallery.*;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Artist;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Artwork;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Expo;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.OwnerArtistPayment;
import com.safonov.galleryservice.ArtGalleryApplication.model.info.ArtworkModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.info.ExpoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtworkRepository artworkRepository;
    private final ExpoRepository expoRepository;
    private final OwnerArtistPaymentRepository ownerArtistPaymentRepository;

    @Autowired
    public ArtistService(@NotNull final ArtistRepository artistRepository,
                         @NotNull final ArtworkRepository artworkRepository,
                         @NotNull final ExpoRepository expoRepository,
                         @NotNull final OwnerArtistPaymentRepository ownerArtistPaymentRepository) {
        this.artistRepository = artistRepository;
        this.artworkRepository = artworkRepository;
        this.expoRepository = expoRepository;
        this.ownerArtistPaymentRepository = ownerArtistPaymentRepository;
    }

    @Transactional
    public ResponseEntity<String> addArtwork(@NotNull final Long artistId,
                                             @NotNull final ArtworkModel model){
        final Artist artist = artistRepository.findById(artistId).orElse(null);
        if (artist == null) {
            return new ResponseEntity<>("Artist doesnt exist", HttpStatus.NOT_FOUND);
        }
        final Artwork artwork = artworkRepository.findById(model.getArtworkId()).orElse(null);
        final Set<Artwork> artworks = artist.getArtworks();
        if (artwork == null) {
            final Artwork newArtwork = new Artwork(model.getName(), model.getInfo(), artist, null);
            artworks.add(artworkRepository.save(newArtwork));
            artistRepository.save(artist);
            return new ResponseEntity<>("New artwork from artist " + artist.getCredentials().getLogin() + "added for expo", HttpStatus.OK);
        } else if (artist.equals(artwork.getArtist())) {
            return new ResponseEntity<>("Artwork " + artwork.getName() + "belongs to another artist", HttpStatus.BAD_GATEWAY);
        } else if (artworks.contains(artwork)) {
            return new ResponseEntity<>("Artwork already existed for this artist", HttpStatus.ALREADY_REPORTED);
        } else {
            artworks.add(artworkRepository.save(artwork));
            artistRepository.save(artist);
            return new ResponseEntity<>("Existed artwork from artist " + artist.getCredentials().getLogin() + "added for expo", HttpStatus.OK);
        }
    }

    public ResponseEntity<String> acceptRoyalties(@NotNull final Long artistId,
                                                  @NotNull final ExpoModel model){
        final Expo closedExpo = expoRepository.findById(model.getExpoId()).orElse(null);
        if (closedExpo == null){
            return new ResponseEntity<>("Expo doesnt exist", HttpStatus.NOT_FOUND);
        } if (!closedExpo.isClosed()){
            return new ResponseEntity<>("Expo with name " + closedExpo.getName() + " hasn't closed", HttpStatus.NOT_ACCEPTABLE);
        }
        final Artist artist = artistRepository.findById(artistId).orElse(null);
        if (artist == null) {
            return new ResponseEntity<>("Artist doesnt exist", HttpStatus.NOT_FOUND);
        }
        final OwnerArtistPayment payment = ownerArtistPaymentRepository.findPaymentByExpo(closedExpo).orElse(null);
        if (payment == null){
            return new ResponseEntity<>("Payment for expo " + closedExpo.getName() + " doesnt exist", HttpStatus.NOT_FOUND);
        }
        artist.getArtworks().clear();
        artistRepository.save(artist);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    public ResponseEntity<Object> getAllArtworks(@NotNull final Long artistId) {
        final Artist artist = artistRepository.findById(artistId).orElse(null);
        if (artist == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        final List<Artwork> artworks = artworkRepository.findArtworksByArtist(artist);
        if (artworks == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(artworks, HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> getExpoArtworks(@NotNull final Long artistId) {
        final Artist artist = artistRepository.findById(artistId).orElse(null);
        if (artist == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        final Set<Artwork> artworks = artist.getArtworks();
        if (artworks == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(artworks, HttpStatus.OK);
        }
    }
}
