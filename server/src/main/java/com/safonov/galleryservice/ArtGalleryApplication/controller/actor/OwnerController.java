package com.safonov.galleryservice.ArtGalleryApplication.controller.actor;

import com.safonov.galleryservice.ArtGalleryApplication.model.logic.*;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.ExpoModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.ReservationModel;
import com.safonov.galleryservice.ArtGalleryApplication.service.actor.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/owner")
public class OwnerController {

    private final OwnerService service;

    @Autowired
    public OwnerController(@NotNull final OwnerService service) {
        this.service = service;
    }

    @PostMapping("/{ownerId}/accept-payment")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<String> acceptPayment(@PathVariable final Long ownerId,
                                                @Valid @RequestBody final ReservationModel model) {
        return service.acceptPayment(ownerId, model);
    }

    @PostMapping("/{ownerId}/create-expo")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<String> createExpo(@PathVariable final Long ownerId,
                                             @Valid @RequestBody final ExpoModel model) {
        return service.createExpo(ownerId, model);
    }

    @PostMapping("/{ownerId}/edit-expo")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<String> editExpo(@PathVariable final Long ownerId,
                                           @Valid @RequestBody EditExpoModel model) {
        return service.editExpo(ownerId, model);
    }

    @PostMapping("/{ownerId}/start-expo")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<String> startExpo(@PathVariable final Long ownerId,
                                            @Valid @RequestBody final ExpoModel model) {
        return service.startExpo(ownerId, model);
    }

    @PostMapping("/{ownerId}/close-expo")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<String> closeExpo(@PathVariable final Long ownerId,
                                            @Valid @RequestBody final ExpoModel model) {
        return service.closeExpo(ownerId, model);
    }

    @PostMapping("/{ownerId}/pay-for-expo")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<String> payForExpo(@PathVariable final Long ownerId,
                                             @Valid @RequestBody final ExpoModel model) {
        return service.payForExpo(ownerId, model);
    }

    @GetMapping("/{ownerId}/new-expos")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Object> getNewExpos() {
        return service.getNewExpos();
    }

    @GetMapping("/{ownerId}/opened-expos")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Object> getOpenedExpos() {
        return service.getOpenedExpos();
    }

    @GetMapping("/{ownerId}/closed-expos")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Object> getClosedExpos() {
        return service.getClosedExpos();
    }
}
