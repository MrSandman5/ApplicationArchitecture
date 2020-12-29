package com.safonov.galleryservice.ArtGalleryApplication.controller;

import com.safonov.galleryservice.ArtGalleryApplication.configuration.Constants;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.*;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.*;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.CredentialsModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class BackendController {

    private final RoleRepository roleRepository;
    private final CredentialsRepository credentialsRepository;
    private final ClientRepository clientRepository;
    private final OwnerRepository ownerRepository;
    private final ArtistRepository artistRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public BackendController(@NotNull final RoleRepository roleRepository,
                             @NotNull final CredentialsRepository credentialsRepository,
                             @NotNull final ClientRepository clientRepository,
                             @NotNull final OwnerRepository ownerRepository,
                             @NotNull final ArtistRepository artistRepository) {
        this.roleRepository = roleRepository;
        this.credentialsRepository = credentialsRepository;
        this.clientRepository = clientRepository;
        this.ownerRepository = ownerRepository;
        this.artistRepository = artistRepository;
    }

    @GetMapping("/roles")
    public ResponseEntity<Object> getRoles() {
        return ResponseEntity.ok(roleRepository.findAll()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList()));
    }

    @GetMapping("/users")
    public ResponseEntity<Object> getUsers() {
        return ResponseEntity.ok(credentialsRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, CredentialsModel.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/{userID}")
    public ResponseEntity<? extends User> getUsers(@PathVariable final Long userID) {
        final Credentials credentials = credentialsRepository.findById(userID).orElse(null);
        if (credentials == null) {
            return ResponseEntity.notFound().build();
        }
        for (final Role role : credentials.getRoles()) {
            final String type = role.getName().toString();
            switch (type) {
                case "owner":
                    return ResponseEntity.ok(ownerRepository.findByCredentials(credentials).get());
                case "artist":
                    return ResponseEntity.ok(artistRepository.findByCredentials(credentials).get());
                default:
                    return ResponseEntity.ok(clientRepository.findByCredentials(credentials).get());
            }
        }
        return ResponseEntity.badRequest().body(null);
    }
}
