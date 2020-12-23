package com.safonov.galleryservice.ArtGalleryApplication.controller.actor;

import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Expo;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Reservation;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Ticket;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.PayForReservationModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.info.ExpoModel;
import com.safonov.galleryservice.ArtGalleryApplication.service.actor.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class ClientController {

    private final ClientService service;

    @Autowired
    public ClientController(@NotNull final ClientService service) {
        this.service = service;
    }

    @Secured("ROLE_CLIENT")
    @PostMapping("/{clientId}/add-ticket")
    public ResponseEntity<String> addTicket(@PathVariable final Long clientId,
                                            @RequestBody final ExpoModel model) {
        return service.addTicket(clientId, model);
    }

    @Secured("ROLE_CLIENT")
    @PostMapping("/{clientId}/create-reservation")
    public ResponseEntity<String> createReservation(@PathVariable final Long clientId) {
        return service.createReservation(clientId);
    }

    @Secured("ROLE_CLIENT")
    @PostMapping("/{clientId}/pay")
    public ResponseEntity<String> payForReservation(@PathVariable final Long clientId,
                                                    @RequestBody final PayForReservationModel model) {
        return service.payForReservation(clientId, model);
    }

    @Secured("ROLE_CLIENT")
    @GetMapping("/{clientId}/tickets")
    public ResponseEntity<List<Ticket>> getTickets(@PathVariable final Long clientId) {
        return service.getTickets(clientId);
    }

    @Secured("ROLE_CLIENT")
    @GetMapping("/{clientId}/new-reservations")
    public ResponseEntity<List<Reservation>> getNewReservation(@PathVariable final Long clientId) {
        return service.getNewReservations(clientId);
    }

    @Secured("ROLE_CLIENT")
    @GetMapping("/{clientId}/payed-reservations")
    public ResponseEntity<List<Reservation>> getPayedReservation(@PathVariable final Long clientId) {
        return service.getPayedReservations(clientId);
    }

    @Secured("ROLE_CLIENT")
    @GetMapping("/{clientId}/expos")
    public ResponseEntity<List<Expo>> getExpos() {
        return service.getNewExpos();
    }

}
