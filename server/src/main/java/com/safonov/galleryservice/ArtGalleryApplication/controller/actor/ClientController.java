package com.safonov.galleryservice.ArtGalleryApplication.controller.actor;

import com.safonov.galleryservice.ArtGalleryApplication.model.logic.PayForReservationModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.ExpoModel;
import com.safonov.galleryservice.ArtGalleryApplication.service.actor.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/client")
@PreAuthorize("hasRole('CLIENT')")
public class ClientController {

    private final ClientService service;

    @Autowired
    public ClientController(@NotNull final ClientService service) {
        this.service = service;
    }

    @PostMapping("/{clientId}/add-ticket")
    public ResponseEntity<String> addTicket(@PathVariable final Long clientId,
                                            @Valid @RequestBody final ExpoModel model) {
        return service.addTicket(clientId, model);
    }

    @PostMapping("/{clientId}/create-reservation")
    public ResponseEntity<String> createReservation(@PathVariable final Long clientId) {
        return service.createReservation(clientId);
    }

    @PostMapping("/{clientId}/pay")
    public ResponseEntity<String> payForReservation(@PathVariable final Long clientId,
                                                    @Valid  @RequestBody final PayForReservationModel model) {
        return service.payForReservation(clientId, model);
    }

    @GetMapping("/{clientId}/tickets")
    public ResponseEntity<Object> getTickets(@PathVariable final Long clientId) {
        return service.getTickets(clientId);
    }

    @GetMapping("/{clientId}/new-reservations")
    public ResponseEntity<Object> getNewReservation(@PathVariable final Long clientId) {
        return service.getNewReservations(clientId);
    }

    @GetMapping("/{clientId}/payed-reservations")
    public ResponseEntity<Object> getPayedReservation(@PathVariable final Long clientId) {
        return service.getPayedReservations(clientId);
    }

    @GetMapping("/{clientId}/expos")
    public ResponseEntity<Object> getExpos() {
        return service.getNewExpos();
    }

}
