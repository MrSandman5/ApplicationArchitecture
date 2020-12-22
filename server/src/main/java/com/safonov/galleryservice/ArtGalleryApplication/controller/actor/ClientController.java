package com.safonov.galleryservice.ArtGalleryApplication.controller.actor;

import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Expo;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Reservation;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Ticket;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.AddTicketModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.CreateReservationModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.PayForReservationModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.response.ApiResponse;
import com.safonov.galleryservice.ArtGalleryApplication.model.response.ResponseOrMessage;
import com.safonov.galleryservice.ArtGalleryApplication.service.actor.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService service;

    @Autowired
    public ClientController(@NotNull final ClientService service) {
        this.service = service;
    }

    @Secured("ROLE_CLIENT")
    @PostMapping("/add-ticket")
    public ApiResponse addTicket(@RequestBody AddTicketModel model) {
        return service.addTicket(model);
    }

    @Secured("ROLE_CLIENT")
    @PostMapping("/create-reservation")
    public ApiResponse createReservation(@RequestBody CreateReservationModel model) {
        return service.createReservation(model);
    }

    @Secured("ROLE_CLIENT")
    @PostMapping("/pay")
    public ApiResponse payForReservation(@RequestBody PayForReservationModel model) {
        return service.payForReservation(model);
    }

    @Secured("ROLE_CLIENT")
    @GetMapping("/tickets")
    public ResponseOrMessage<List<Ticket>> getTickets(@RequestBody Map<String, Long> clientId) {
        return service.getTickets(clientId);
    }

    @Secured("ROLE_CLIENT")
    @GetMapping("/new-reservations")
    public ResponseOrMessage<List<Reservation>> getNewReservation(@RequestBody Map<String, Long> clientId) {
        return service.getNewReservations(clientId);
    }

    @Secured("ROLE_CLIENT")
    @GetMapping("/payed-reservations")
    public ResponseOrMessage<List<Reservation>> getPayedReservation(@RequestBody Map<String, Long> clientId) {
        return service.getPayedReservations(clientId);
    }

    @Secured("ROLE_CLIENT")
    @GetMapping("/expos")
    public ResponseOrMessage<List<Expo>> getExpos() {
        return service.getNewExpos();
    }

}
