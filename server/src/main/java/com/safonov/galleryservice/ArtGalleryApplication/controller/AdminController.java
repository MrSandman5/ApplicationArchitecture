package com.safonov.galleryservice.ArtGalleryApplication.controller;

import com.safonov.galleryservice.ArtGalleryApplication.service.AdminService;
import com.safonov.galleryservice.ArtGalleryApplication.service.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService service;
    private final DataGenerator generator;

    @Autowired
    public AdminController(@NotNull final AdminService service,
                           @NotNull final DataGenerator generator) {
        this.service = service;
        this.generator = generator;
    }

    @DeleteMapping("/deleteUser/{userId}/{userType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable final Long userId,
                                             @PathVariable final String userType) {
        return service.deletePerson(userId, userType);
    }

    @GetMapping("/generate/{count}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> generate(@PathVariable final Long count) {
        return new ResponseEntity<>(generator.generateFakeDataForClient(count) +
                generator.generateFakeDataForOwner(count) +
                generator.generateFakeDataForArtist(count), HttpStatus.OK);
    }
}
