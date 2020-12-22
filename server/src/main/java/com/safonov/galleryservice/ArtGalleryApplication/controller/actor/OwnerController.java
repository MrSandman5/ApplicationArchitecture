package com.safonov.galleryservice.ArtGalleryApplication.controller.actor;

import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Expo;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.*;
import com.safonov.galleryservice.ArtGalleryApplication.model.response.ApiResponse;
import com.safonov.galleryservice.ArtGalleryApplication.model.response.ResponseOrMessage;
import com.safonov.galleryservice.ArtGalleryApplication.service.DataGenerator;
import com.safonov.galleryservice.ArtGalleryApplication.service.actor.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    private final OwnerService service;

    @Autowired
    public OwnerController(@NotNull final OwnerService service) {
        this.service = service;
    }

    @Secured("ROLE_OWNER")
    @PostMapping("/accept-payment")
    public ApiResponse acceptPayment(@RequestBody AcceptPaymentModel model) {
        return service.acceptPayment(model);
    }

    @Secured("ROLE_OWNER")
    @PostMapping("/create-expo")
    public ApiResponse createExpo(@RequestBody CreateExpoModel model) {
        return service.createExpo(model);
    }

    @Secured("ROLE_OWNER")
    @PostMapping("/edit-expo")
    public ApiResponse editExpo(@RequestBody EditExpoModel model) {
        return service.editExpo(model);
    }

    @Secured("ROLE_OWNER")
    @PostMapping("/start-expo")
    public ApiResponse startExpo(@RequestBody StartCloseExpoModel model) {
        return service.startExpo(model);
    }

    @Secured("ROLE_OWNER")
    @PostMapping("/close-expo")
    public ApiResponse closeExpo(@RequestBody StartCloseExpoModel model) {
        return service.closeExpo(model);
    }

    @Secured("ROLE_OWNER")
    @PostMapping("/pay-for-expo")
    public ApiResponse payForExpo(@RequestBody PayForExpoModel model) {
        return service.payForExpo(model);
    }

    @Secured("ROLE_OWNER")
    @GetMapping("/new-expos")
    public ResponseOrMessage<List<Expo>> getNewExpos() {
        return service.getNewExpos();
    }

    @Secured("ROLE_OWNER")
    @GetMapping("/opened-expos")
    public ResponseOrMessage<List<Expo>> getOpenedExpos() {
        return service.getOpenedExpos();
    }

    @Secured("ROLE_OWNER")
    @GetMapping("/closed-expos")
    public ResponseOrMessage<List<Expo>> getClosedExpos() {
        return service.getClosedExpos();
    }
}
