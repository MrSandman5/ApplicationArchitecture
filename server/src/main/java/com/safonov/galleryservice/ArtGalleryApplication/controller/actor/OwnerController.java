package com.safonov.galleryservice.ArtGalleryApplication.controller.actor;

import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.*;
import com.safonov.galleryservice.ArtGalleryApplication.model.info.ExpoModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.info.ReservationModel;
import com.safonov.galleryservice.ArtGalleryApplication.service.actor.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    private final OwnerService service;

    @Autowired
    public OwnerController(@NotNull final OwnerService service) {
        this.service = service;
    }

    @Secured("ROLE_OWNER")
    @PostMapping("/{ownerId}/accept-payment")
    public ResponseEntity<String> acceptPayment(@PathVariable final Long ownerId,
                                                @Valid @RequestBody final ReservationModel model) {
        return service.acceptPayment(ownerId, model);
    }

    @Secured("ROLE_OWNER")
    @PostMapping("/{ownerId}/create-expo")
    public ResponseEntity<String> createExpo(@PathVariable final Long ownerId,
                                             @Valid @RequestBody final ExpoModel model) {
        return service.createExpo(ownerId, model);
    }

    @Secured("ROLE_OWNER")
    @PostMapping("/{ownerId}/edit-expo")
    public ResponseEntity<String> editExpo(@PathVariable final Long ownerId,
                                           @Valid @RequestBody EditExpoModel model) {
        return service.editExpo(ownerId, model);
    }

    @Secured("ROLE_OWNER")
    @PostMapping("/{ownerId}/start-expo")
    public ResponseEntity<String> startExpo(@PathVariable final Long ownerId,
                                            @Valid @RequestBody final ExpoModel model) {
        return service.startExpo(ownerId, model);
    }

    @Secured("ROLE_OWNER")
    @PostMapping("/{ownerId}/close-expo")
    public ResponseEntity<String> closeExpo(@PathVariable final Long ownerId,
                                            @Valid @RequestBody final ExpoModel model) {
        return service.closeExpo(ownerId, model);
    }

    @Secured("ROLE_OWNER")
    @PostMapping("/{ownerId}/pay-for-expo")
    public ResponseEntity<String> payForExpo(@PathVariable final Long ownerId,
                                             @Valid @RequestBody final ExpoModel model) {
        return service.payForExpo(ownerId, model);
    }

    @Secured("ROLE_OWNER")
    @GetMapping("/{ownerId}/new-expos")
    public ResponseEntity<Object> getNewExpos() {
        return service.getNewExpos();
    }

    @Secured("ROLE_OWNER")
    @GetMapping("/{ownerId}/opened-expos")
    public ResponseEntity<Object> getOpenedExpos() {
        return service.getOpenedExpos();
    }

    @Secured("ROLE_OWNER")
    @GetMapping("/{ownerId}/closed-expos")
    public ResponseEntity<Object> getClosedExpos() {
        return service.getClosedExpos();
    }
}
