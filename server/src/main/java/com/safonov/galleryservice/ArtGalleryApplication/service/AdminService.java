package com.safonov.galleryservice.ArtGalleryApplication.service;

import com.safonov.galleryservice.ArtGalleryApplication.data.actor.*;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Artist;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Client;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Credentials;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class AdminService {

    private final CredentialsRepository credentialsRepository;
    private final ClientRepository clientRepository;
    private final OwnerRepository ownerRepository;
    private final ArtistRepository artistRepository;

    @Autowired
    public AdminService(@NotNull final CredentialsRepository credentialsRepository,
                        @NotNull final ClientRepository clientRepository,
                        @NotNull final OwnerRepository ownerRepository,
                        @NotNull final ArtistRepository artistRepository) {
        this.credentialsRepository = credentialsRepository;
        this.clientRepository = clientRepository;
        this.ownerRepository = ownerRepository;
        this.artistRepository = artistRepository;
    }

    public ResponseEntity<String> deleteUser(@NotNull final Long userId,
                                             @NotNull final String userType) {
        final Credentials credentials = credentialsRepository.findById(userId).orElse(null);
        if (credentials == null) {
            return new ResponseEntity<>("Credentials not found", HttpStatus.NOT_FOUND);
        }
        switch (userType) {
            case "client":
                final Client client = clientRepository.findByCredentials(credentials).orElse(null);
                if (client == null) {
                    return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
                }
                credentialsRepository.delete(credentials);
                clientRepository.delete(client);
                return new ResponseEntity<>("Client was deleted", HttpStatus.OK);

            case "owner":
                final Owner owner = ownerRepository.findByCredentials(credentials).orElse(null);
                if (owner == null) {
                    return new ResponseEntity<>("Owner not found", HttpStatus.NOT_FOUND);
                }
                credentialsRepository.delete(credentials);
                ownerRepository.delete(owner);
                return new ResponseEntity<>("Owner was deleted", HttpStatus.OK);
            case "artist":
                final Artist artist = artistRepository.findByCredentials(credentials).orElse(null);
                if (artist == null) {
                    return new ResponseEntity<>("Artist not found", HttpStatus.NOT_FOUND);
                }
                credentialsRepository.delete(credentials);
                artistRepository.delete(artist);
                return new ResponseEntity<>("Artist was deleted", HttpStatus.OK);
            default:
                return new ResponseEntity<>("Wrong parameter", HttpStatus.BAD_REQUEST);
        }
    }
}
