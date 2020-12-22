package com.safonov.galleryservice.ArtGalleryApplication.controller;

import com.safonov.galleryservice.ArtGalleryApplication.model.response.ApiResponse;
import com.safonov.galleryservice.ArtGalleryApplication.service.AdminService;
import com.safonov.galleryservice.ArtGalleryApplication.service.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ApiResponse deleteUser(@PathVariable Long userId, @PathVariable String userType) {
        return service.deletePerson(userId, userType);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/generate/{count}")
    public ApiResponse generate(@PathVariable int count) {
        return new ApiResponse(generator.generateFakeDataForClient(count) +
                generator.generateFakeDataForArtist(count));
    }
}
