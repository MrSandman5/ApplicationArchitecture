package com.safonov.galleryservice.ArtGalleryApplication.controller.actor;

import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Owner;
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
@PreAuthorize("hasRole('OWNER')")
public class OwnerController {

    private final OwnerService service;

    @Autowired
    public OwnerController(@NotNull final OwnerService service) {
        this.service = service;
    }

    @PostMapping("/{ownerId}/accept-payment")
    public ResponseEntity<String> acceptPayment(@PathVariable final Long ownerId,
                                                @Valid @RequestBody final ReservationModel model) {
        return service.acceptPayment(ownerId, model);
    }

    @PostMapping("/{ownerId}/create-expo")
    public ResponseEntity<String> createExpo(@PathVariable final Long ownerId,
                                             @Valid @RequestBody final ExpoModel model) {
        return service.createExpo(ownerId, model);
    }

    @PostMapping("/{ownerId}/edit-expo")
    public ResponseEntity<String> editExpo(@PathVariable final Long ownerId,
                                           @Valid @RequestBody EditExpoModel model) {
        return service.editExpo(ownerId, model);
    }

    @PostMapping("/{ownerId}/start-expo")
    public ResponseEntity<String> startExpo(@PathVariable final Long ownerId,
                                            @Valid @RequestBody final ExpoModel model) {
        return service.startExpo(ownerId, model);
    }

    @PostMapping("/{ownerId}/close-expo")
    public ResponseEntity<String> closeExpo(@PathVariable final Long ownerId,
                                            @Valid @RequestBody final ExpoModel model) {
        return service.closeExpo(ownerId, model);
    }

    @PostMapping("/{ownerId}/pay-for-expo")
    public ResponseEntity<String> payForExpo(@PathVariable final Long ownerId,
                                             @Valid @RequestBody final ExpoModel model) {
        return service.payForExpo(ownerId, model);
    }

    @GetMapping("/{ownerId}/all-expos")
    public ResponseEntity<Object> getAllExpos() {
        return service.getAllExpos();
    }

    @GetMapping("/{ownerId}/new-expos")
    public ResponseEntity<Object> getNewExpos() {
        return service.getNewExpos();
    }

    @GetMapping("/{ownerId}/opened-expos")
    public ResponseEntity<Object> getOpenedExpos() {
        return service.getOpenedExpos();
    }

    @GetMapping("/{ownerId}/closed-expos")
    public ResponseEntity<Object> getClosedExpos() {
        return service.getClosedExpos();
    }

    @GetMapping("/{ownerId}")
    public ResponseEntity<Owner> getOwner(@PathVariable final Long ownerId) {
        return service.getOwner(ownerId);
    }
}
