package com.safonov.galleryservice.ArtGalleryApplication.service;

import com.safonov.galleryservice.ArtGalleryApplication.data.actor.*;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Artist;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Client;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Owner;
import com.safonov.galleryservice.ArtGalleryApplication.model.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class AdminService {

    private final ClientRepository clientRepository;
    private final OwnerRepository ownerRepository;
    private final ArtistRepository artistRepository;

    @Autowired
    public AdminService(@NotNull final ClientRepository clientRepository,
                        @NotNull final OwnerRepository ownerRepository,
                        @NotNull final ArtistRepository artistRepository) {
        this.clientRepository = clientRepository;
        this.ownerRepository = ownerRepository;
        this.artistRepository = artistRepository;
    }

    public ApiResponse deletePerson(@NotNull final Long userId, @NotNull final String userType) {
        switch (userType) {
            case "ROLE_CLIENT":
                final Client client = clientRepository.findById(userId).orElse(null);
                if (client == null) {
                    return new ApiResponse("Client not found");
                }
                clientRepository.delete(client);
                return new ApiResponse("Client was deleted");

            case "ROLE_OWNER":
                final Owner owner = ownerRepository.findById(userId).orElse(null);
                if (owner == null) {
                    return new ApiResponse("Owner not found");
                }
                ownerRepository.save(owner);
                return new ApiResponse("Owner was deleted");
            case "ROLE_ARTIST":
                final Artist artist = artistRepository.findById(userId).orElse(null);
                if (artist == null) {
                    return new ApiResponse("Artist not found");
                }
                artistRepository.save(artist);
                return new ApiResponse("Artist was deleted");
            default:
                return new ApiResponse("Wrong parameter");
        }
    }
}
