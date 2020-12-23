package com.safonov.galleryservice.ArtGalleryApplication.service.actor;

import com.safonov.galleryservice.ArtGalleryApplication.configuration.Constants;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.ClientRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.OwnerRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.gallery.ClientOwnerPaymentRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.gallery.ExpoRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.gallery.ReservationRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.gallery.TicketRepository;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Client;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Owner;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.ClientOwnerPayment;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Expo;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Reservation;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Ticket;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.PayForReservationModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.info.ExpoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final OwnerRepository ownerRepository;
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;
    private final ExpoRepository expoRepository;
    private final ClientOwnerPaymentRepository clientOwnerPaymentRepository;

    @Autowired
    public ClientService(@NotNull final ClientRepository clientRepository,
                         @NotNull final OwnerRepository ownerRepository,
                         @NotNull final TicketRepository ticketRepository,
                         @NotNull final ReservationRepository reservationRepository,
                         @NotNull final ExpoRepository expoRepository,
                         @NotNull final ClientOwnerPaymentRepository clientOwnerPaymentRepository) {
        this.clientRepository = clientRepository;
        this.ownerRepository = ownerRepository;
        this.ticketRepository = ticketRepository;
        this.reservationRepository = reservationRepository;
        this.expoRepository = expoRepository;
        this.clientOwnerPaymentRepository = clientOwnerPaymentRepository;
    }

    @Transactional
    public ResponseEntity<String> addTicket(@NotNull final Long clientId,
                                            @NotNull final ExpoModel model){
        final Expo ticketExpo = expoRepository.findById(model.getExpoId()).orElse(null);
        if (ticketExpo == null){
            return new ResponseEntity<>("Expo doesnt exist", HttpStatus.NOT_FOUND);
        }
        else if (ticketExpo.isClosed()){
            return new ResponseEntity<>("Expo with name " + ticketExpo.getName() + " already closed", HttpStatus.ALREADY_REPORTED);
        }
        final Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            return new ResponseEntity<>("Client doesnt exist", HttpStatus.NOT_FOUND);
        }
        final List<Ticket> tickets = client.getTickets();
        if (!tickets.isEmpty()){
            for (final Ticket tick : tickets) {
                if (!ticketExpo.equals(tick.getExpo())){
                    return new ResponseEntity<>("Must add ticket for one expo at a time", HttpStatus.BAD_REQUEST);
                }
            }
        }
        final Ticket newTicket = new Ticket(client, ticketExpo, ticketExpo.getTicketPrice());
        tickets.add(ticketRepository.save(newTicket));
        clientRepository.save(client);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> createReservation(@NotNull final Long clientId){
        final Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            return new ResponseEntity<>("Client doesnt exist", HttpStatus.NOT_FOUND);
        }
        final Expo expo = expoRepository.findById(client.getTickets().get(0).getExpo().getId()).orElse(null);
        if (expo == null){
            return new ResponseEntity<>("Expo doesnt exist", HttpStatus.NOT_FOUND);
        }
        else if (expo.isClosed()){
            return new ResponseEntity<>("Expo with name " + expo.getName() + " already closed", HttpStatus.ALREADY_REPORTED);
        }
        final Reservation reservation = new Reservation(client, expo.getStartTime());
        client.getReservations().add(reservationRepository.save(reservation));
        client.getTickets().clear();
        clientRepository.save(client);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> payForReservation(@NotNull final Long clientId,
                                                    @NotNull final PayForReservationModel model){
        final Reservation clientReservation = reservationRepository.findById(model.getReservation().getReservationId()).orElse(null);
        if (clientReservation == null){
            return new ResponseEntity<>("Reservation doesnt exist", HttpStatus.NOT_FOUND);
        }
        if (clientReservation.isClosed()){
            return new ResponseEntity<>("Reservation is already closed", HttpStatus.ALREADY_REPORTED);
        }

        final Expo ticketExpo = expoRepository.findById(clientReservation.getTickets().get(0).getExpo().getId()).orElse(null);
        if (ticketExpo == null){
            return new ResponseEntity<>("Expo doesnt exist", HttpStatus.NOT_FOUND);
        } else if (ticketExpo.isClosed()){
            return new ResponseEntity<>("Expo with name " + ticketExpo.getName() + " already closed", HttpStatus.ALREADY_REPORTED);
        }
        final Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            return new ResponseEntity<>("Client doesnt exist", HttpStatus.NOT_FOUND);
        }
        if (LocalDateTime.now().isAfter(ticketExpo.getStartTime())){
            client.getReservations().remove(clientReservation);
            clientRepository.save(client);
            return new ResponseEntity<>("Expo with name" + ticketExpo.getName() + " already started", HttpStatus.ALREADY_REPORTED);
        }
        clientReservation.setStatus(Constants.ReservationStatus.Payed);
        reservationRepository.save(clientReservation);
        final Owner owner = ownerRepository.findById(model.getOwnerId()).orElse(null);
        if (owner == null) {
            return new ResponseEntity<>("Owner doesnt exist", HttpStatus.NOT_FOUND);
        }
        final ClientOwnerPayment ticketPayment = new ClientOwnerPayment(clientReservation, client, owner);
        client.getReservations().remove(clientReservation);
        clientRepository.save(client);
        clientOwnerPaymentRepository.save(ticketPayment);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    public ResponseEntity<List<Ticket>> getTickets(@NotNull final Long clientId) {
        final Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        final List<Ticket> tickets = ticketRepository.findTicketsByClient(client);
        if (tickets == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    public ResponseEntity<List<Reservation>> getNewReservations(@NotNull final Long clientId) {
        final Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        final List<Reservation> reservations = reservationRepository.findReservationsByClientAndStatus(client,  Constants.ReservationStatus.New);
        if (reservations == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        }
    }

    public ResponseEntity<List<Reservation>> getPayedReservations(@NotNull final Long clientId) {
        final Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        final List<Reservation> reservations = reservationRepository.findReservationsByClientAndStatus(client,  Constants.ReservationStatus.Payed);
        if (reservations == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        }
    }

    public ResponseEntity<List<Expo>> getNewExpos() {
        final List<Expo> expos = expoRepository.findExposByStatus(Constants.ExpoStatus.New);
        if (expos == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(expos, HttpStatus.OK);
        }
    }

}
