package com.safonov.galleryservice.ArtGalleryApplication.controller;

import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Role;
import com.safonov.galleryservice.ArtGalleryApplication.service.AdminService;
import com.safonov.galleryservice.ArtGalleryApplication.service.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
public class AdminController {

    private final AdminService service;
    private final DataGenerator generator;

    @Autowired
    public AdminController(@NotNull final AdminService service,
                           @NotNull final DataGenerator generator) {
        this.service = service;
        this.generator = generator;
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/deletePerson/{userId}/{userType}")
    public ResponseEntity<String> deleteUser(@PathVariable final Long userId,
                                             @PathVariable final String userType) {
        return service.deletePerson(userId, userType);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/generate/{count}")
    public ResponseEntity<String> generate(@PathVariable final int count) {
        return new ResponseEntity<>(generator.generateFakeDataForClient(count) +
                generator.generateFakeDataForOwner(count) +
                generator.generateFakeDataForArtist(count), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/roles")
    public ResponseEntity<Object> getRoles() {
        return service.getRoles();
    }
}
