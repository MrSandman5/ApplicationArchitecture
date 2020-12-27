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
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.ReservationModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.TicketModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.logic.PayForReservationModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.ExpoModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final OwnerRepository ownerRepository;
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;
    private final ExpoRepository expoRepository;
    private final ClientOwnerPaymentRepository clientOwnerPaymentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ClientService(@NotNull final ClientRepository clientRepository,
                         @NotNull final OwnerRepository ownerRepository,
                         @NotNull final TicketRepository ticketRepository,
                         @NotNull final ReservationRepository reservationRepository,
                         @NotNull final ExpoRepository expoRepository,
                         @NotNull final ClientOwnerPaymentRepository clientOwnerPaymentRepository,
                         @NotNull final ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.ownerRepository = ownerRepository;
        this.ticketRepository = ticketRepository;
        this.reservationRepository = reservationRepository;
        this.expoRepository = expoRepository;
        this.clientOwnerPaymentRepository = clientOwnerPaymentRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public ResponseEntity<String> addTicket(@NotNull final Long clientId,
                                            @NotNull final ExpoModel model) {
        final Expo ticketExpo = expoRepository.findExpoByName(model.getName()).orElse(null);
        if (ticketExpo == null){
            return new ResponseEntity<>("Expo doesnt exist", HttpStatus.NOT_FOUND);
        }
        else if (ticketExpo.isClosed()){
            return new ResponseEntity<>("Expo with name " + ticketExpo.getName() + " already closed", HttpStatus.BAD_REQUEST);
        }
        final Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            return new ResponseEntity<>("Client doesnt exist", HttpStatus.NOT_FOUND);
        }
        final List<Ticket> tickets = ticketRepository.findTicketsByClient(client);
        if (!tickets.isEmpty()){
            for (final Ticket tick : tickets) {
                if (!ticketExpo.equals(tick.getExpo())){
                    return new ResponseEntity<>("Must add ticket for one expo at a time", HttpStatus.BAD_REQUEST);
                }
            }
        }
        ticketRepository.save(new Ticket(client, ticketExpo, ticketExpo.getTicketPrice()));
        return new ResponseEntity<>("", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> createReservation(@NotNull final Long clientId) {
        final Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            return new ResponseEntity<>("Client doesnt exist", HttpStatus.NOT_FOUND);
        }
        final Expo expo = expoRepository.findById(
                ticketRepository.findTicketsByClient(client).stream()
                        .findFirst().get().getExpo().getId()).orElse(null);
        if (expo == null){
            return new ResponseEntity<>("Expo doesnt exist", HttpStatus.NOT_FOUND);
        }
        else if (expo.isClosed()){
            return new ResponseEntity<>("Expo with name " + expo.getName() + " already closed", HttpStatus.BAD_REQUEST);
        }
        final Reservation addedReservation = reservationRepository.save(new Reservation(client, expo.getStartTime()));
        double cost = 0;
        for (final Ticket ticket : ticketRepository.findTicketsByExpo(expo)) {
            ticket.setReservation(addedReservation);
            ticketRepository.save(ticket);
            cost += ticket.getCost();
        }
        addedReservation.setCost(cost);
        reservationRepository.save(addedReservation);
        return new ResponseEntity<>("", HttpStatus.CREATED);
    }

    public ResponseEntity<String> payForReservation(@NotNull final Long clientId,
                                                    @NotNull final PayForReservationModel model) {
        final Reservation clientReservation = reservationRepository.findById(model.getReservation().getReservationId()).orElse(null);
        if (clientReservation == null){
            return new ResponseEntity<>("Reservation doesnt exist", HttpStatus.NOT_FOUND);
        }
        if (clientReservation.isClosed()){
            return new ResponseEntity<>("Reservation is already closed", HttpStatus.BAD_REQUEST);
        }

        final Expo ticketExpo = expoRepository.findById(
                ticketRepository.findTicketsByReservation(clientReservation)
                        .stream().findFirst().get().getExpo().getId()).orElse(null);
        if (ticketExpo == null){
            return new ResponseEntity<>("Expo doesnt exist", HttpStatus.NOT_FOUND);
        } else if (ticketExpo.isClosed()){
            return new ResponseEntity<>("Expo with name " + ticketExpo.getName() + " already closed", HttpStatus.BAD_REQUEST);
        }
        final Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            return new ResponseEntity<>("Client doesnt exist", HttpStatus.NOT_FOUND);
        }
        if (LocalDateTime.now().isAfter(ticketExpo.getStartTime())){
            clientReservation.setStatus(Constants.ReservationStatus.Closed);
            reservationRepository.save(clientReservation);
            return new ResponseEntity<>("Expo with name" + ticketExpo.getName() + " already started", HttpStatus.BAD_REQUEST);
        }
        clientReservation.setStatus(Constants.ReservationStatus.Payed);
        reservationRepository.save(clientReservation);
        final Owner owner = ownerRepository.findById(model.getOwnerId()).orElse(null);
        if (owner == null) {
            return new ResponseEntity<>("Owner doesnt exist", HttpStatus.NOT_FOUND);
        }
        clientOwnerPaymentRepository.save(new ClientOwnerPayment(clientReservation, client, owner));
        return new ResponseEntity<>("", HttpStatus.CREATED);
    }

    public ResponseEntity<Object> getTickets(@NotNull final Long clientId) {
        final Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        final List<Ticket> tickets = ticketRepository.findTicketsByClient(client);
        if (tickets == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else return new ResponseEntity<>(tickets.stream()
                .map(ticket -> modelMapper.map(ticket, TicketModel.class))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<Object> getNewReservations(@NotNull final Long clientId) {
        final Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        final List<Reservation> reservations = reservationRepository.findReservationsByClientAndStatus(client, Constants.ReservationStatus.New);
        if (reservations == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(reservations.stream()
                    .map(reservation -> modelMapper.map(reservation, ReservationModel.class))
                    .collect(Collectors.toList()), HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> getPayedReservations(@NotNull final Long clientId) {
        final Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        final List<Reservation> reservations = reservationRepository.findReservationsByClientAndStatus(client,  Constants.ReservationStatus.Payed);
        if (reservations == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(reservations.stream()
                    .map(reservation -> modelMapper.map(reservation, ReservationModel.class))
                    .collect(Collectors.toList()), HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> getNewExpos() {
        final List<Expo> expos = expoRepository.findExposByStatus(Constants.ExpoStatus.New);
        if (expos == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(expos.stream()
                    .map(expo -> modelMapper.map(expo, ExpoModel.class))
                    .collect(Collectors.toList()), HttpStatus.OK);
        }
    }

}
