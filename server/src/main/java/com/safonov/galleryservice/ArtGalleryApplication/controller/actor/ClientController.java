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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@RestController
public class ClientController {

    private final ClientService service;

    @Autowired
    public ClientController(@NotNull final ClientService service) {
        this.service = service;
    }

    @PostMapping("/add-ticket")
    public ApiResponse addTicket(@RequestBody AddTicketModel model) {
        return service.addTicket(model);
    }

    @PostMapping("/create-reservation")
    public ApiResponse createReservation(@RequestBody CreateReservationModel model) {
        return service.createReservation(model);
    }

    @PostMapping("/pay")
    public ApiResponse payForReservation(@RequestBody PayForReservationModel model) {
        return service.payForReservation(model);
    }

    @GetMapping("/tickets")
    public ResponseOrMessage<List<Ticket>> getTickets(@RequestBody Map<String, Long> clientId) {
        return service.getTickets(clientId);
    }

    @GetMapping("/new-reservations")
    public ResponseOrMessage<List<Reservation>> getNewReservation(@RequestBody Map<String, Long> clientId) {
        return service.getNewReservations(clientId);
    }

    @GetMapping("/payed-reservations")
    public ResponseOrMessage<List<Reservation>> getPayedReservation(@RequestBody Map<String, Long> clientId) {
        return service.getPayedReservations(clientId);
    }

    @GetMapping("/expos")
    public ResponseOrMessage<List<Expo>> getExpos() {
        return service.getNewExpos();
    }

}
