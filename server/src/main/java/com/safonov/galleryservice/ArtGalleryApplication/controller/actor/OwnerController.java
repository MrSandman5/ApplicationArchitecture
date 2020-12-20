package com.safonov.galleryservice.ArtGalleryApplication.controller.actor;

import com.safonov.galleryservice.ArtGalleryApplication.configuration.Constants;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Expo;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.*;
import com.safonov.galleryservice.ArtGalleryApplication.model.response.ApiResponse;
import com.safonov.galleryservice.ArtGalleryApplication.model.response.ResponseOrMessage;
import com.safonov.galleryservice.ArtGalleryApplication.service.DataGenerator;
import com.safonov.galleryservice.ArtGalleryApplication.service.actor.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class OwnerController {

    private final OwnerService service;
    private final DataGenerator generator;

    @Autowired
    public OwnerController(@NotNull final OwnerService service,
                           @NotNull final DataGenerator generator) {
        this.service = service;
        this.generator = generator;
    }

    @PostMapping("/accept-payment")
    public ApiResponse acceptPayment(@RequestBody AcceptPaymentModel model) {
        return service.acceptPayment(model);
    }

    @PostMapping("/create-expo")
    public ApiResponse createExpo(@RequestBody CreateExpoModel model) {
        return service.createExpo(model);
    }

    @PostMapping("/edit-expo")
    public ApiResponse editExpo(@RequestBody EditExpoModel model) {
        return service.editExpo(model);
    }

    @PostMapping("/start-expo")
    public ApiResponse startExpo(@RequestBody StartCloseExpoModel model) {
        return service.startExpo(model);
    }

    @PostMapping("/close-expo")
    public ApiResponse closeExpo(@RequestBody StartCloseExpoModel model) {
        return service.closeExpo(model);
    }

    @PostMapping("/pay-for-expo")
    public ApiResponse payForExpo(@RequestBody PayForExpoModel model) {
        return service.payForExpo(model);
    }

    @GetMapping("/new-expos")
    public ResponseOrMessage<List<Expo>> getNewExpos() {
        return service.getNewExpos();
    }

    @GetMapping("/opened-expos")
    public ResponseOrMessage<List<Expo>> getOpenedExpos() {
        return service.getOpenedExpos();
    }

    @GetMapping("/closed-expos")
    public ResponseOrMessage<List<Expo>> getClosedExpos() {
        return service.getClosedExpos();
    }

    @DeleteMapping("/deletePerson/{personId}/{personType}")
    public ApiResponse deletePerson(@PathVariable Long personId, @PathVariable Constants.UserType personType) {
        return service.deletePerson(personId, personType);
    }

    @GetMapping("/generate/{count}")
    public ApiResponse generate(@PathVariable int count) {
        return new ApiResponse(generator.generateFakeDataForClient(count) +
                generator.generateFakeDataForArtist(count));
    }
}
