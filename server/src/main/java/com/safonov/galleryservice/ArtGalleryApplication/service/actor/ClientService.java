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
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.AddTicketModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.CreateReservationModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.PayForReservationModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.response.ApiResponse;
import com.safonov.galleryservice.ArtGalleryApplication.model.response.ResponseOrMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final OwnerRepository ownerRepository;
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;
    private final ExpoRepository expoRepository;
    private final ClientOwnerPaymentRepository clientOwnerPaymentRepository;

    @Autowired
    ClientService(@NotNull final ClientRepository clientRepository,
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
    public ApiResponse addTicket(@NotNull final AddTicketModel model){
        final Expo ticketExpo = expoRepository.findById(model.getExpo().getExpoId()).orElse(null);
        if (ticketExpo == null){
            return new ApiResponse("Expo doesnt exist");
        }
        else if (ticketExpo.isClosed()){
            return new ApiResponse("Expo with name " + ticketExpo.getName() + " already closed");
        }
        final Client client = clientRepository.findById(model.getClientId()).orElse(null);
        if (client == null) {
            return new ApiResponse("Client doesnt exist");
        } else if (!client.getAuthenticated()) {
            return new ApiResponse("Client wasnt authenticated");
        }
        final List<Ticket> tickets = client.getTickets();
        if (!tickets.isEmpty()){
            for (final Ticket tick : tickets) {
                if (!ticketExpo.equals(tick.getExpo())){
                    return new ApiResponse("Must add ticket for one expo at a time");
                }
            }
        }
        final Ticket newTicket = new Ticket(client, ticketExpo, ticketExpo.getTicketPrice());
        tickets.add(ticketRepository.save(newTicket));
        clientRepository.save(client);
        return new ApiResponse("New ticket was added to client");
    }

    @Transactional
    public ApiResponse createReservation(@NotNull final CreateReservationModel model){
        final Client client = clientRepository.findById(model.getClientId()).orElse(null);
        if (client == null) {
            return new ApiResponse("Client doesnt exist");
        } else if (!client.getAuthenticated()) {
            return new ApiResponse("Client wasnt authenticated");
        }
        final Expo expo = expoRepository.findById(client.getTickets().get(0).getExpo().getId()).orElse(null);
        if (expo == null){
            return new ApiResponse("Expo doesnt exist");
        }
        else if (expo.isClosed()){
            return new ApiResponse("Expo with name " + expo.getName() + " already closed");
        }
        final Reservation reservation = new Reservation(client, expo.getStartTime());
        client.getReservations().add(reservationRepository.save(reservation));
        client.getTickets().clear();
        clientRepository.save(client);
        return new ApiResponse("New reservation was created for client");
    }

    @Transactional
    public ApiResponse payForReservation(@NotNull final PayForReservationModel model){
        final Reservation clientReservation = reservationRepository.findById(model.getReservation().getReservationId()).orElse(null);
        if (clientReservation == null){
            return new ApiResponse("Reservation doesnt exist");
        }
        if (clientReservation.isClosed()){
            return new ApiResponse("Reservation is already closed");
        }

        final Expo ticketExpo = expoRepository.findById(clientReservation.getTickets().get(0).getExpo().getId()).orElse(null);
        if (ticketExpo == null){
            return new ApiResponse("Expo doesnt exist");
        } else if (ticketExpo.isClosed()){
            return new ApiResponse("Expo with name " + ticketExpo.getName() + " already closed");
        }
        final Client client = clientRepository.findById(model.getClientId()).orElse(null);
        if (client == null) {
            return new ApiResponse("Client doesnt exist");
        } else if (!client.getAuthenticated()) {
            return new ApiResponse("Client wasnt authenticated");
        }
        if (LocalDateTime.now().isAfter(ticketExpo.getStartTime())){
            client.getReservations().remove(clientReservation);
            clientRepository.save(client);
            return new ApiResponse("Expo with name" + ticketExpo.getName() + " already started");
        }
        clientReservation.setStatus(Constants.ReservationStatus.Payed);
        reservationRepository.save(clientReservation);
        final Owner owner = ownerRepository.findById(model.getOwnerId()).orElse(null);
        if (owner == null) {
            return new ApiResponse("Owner doesnt exist");
        }
        final ClientOwnerPayment ticketPayment = new ClientOwnerPayment(clientReservation, client, owner);
        client.getReservations().remove(clientReservation);
        clientRepository.save(client);
        clientOwnerPaymentRepository.save(ticketPayment);
        return new ApiResponse("Client " + client.getCredentials().getLogin() + " payed for reservation");
    }

    public ResponseOrMessage<List<Ticket>> getTickets(@NotNull final Map<String, Long> clientId) {
        if (clientId.containsKey("clientId")) {
            final Client client = clientRepository.findById(clientId.get("clientId")).orElse(null);
            if (client == null) {
                return new ResponseOrMessage<>("Client doesnt exist");
            }
            final List<Ticket> tickets = ticketRepository.findTicketsByClient(client);
            if (tickets == null) {
                return new ResponseOrMessage<>("Client does not have active tickets");
            } else return new ResponseOrMessage<>(tickets);

        } else return new ResponseOrMessage<>("Wrong parameter");
    }

    public ResponseOrMessage<List<Reservation>> getNewReservations(@NotNull final Map<String, Long> clientId) {
        if (clientId.containsKey("clientId")) {
            final Client client = clientRepository.findById(clientId.get("clientId")).orElse(null);
            if (client == null) {
                return new ResponseOrMessage<>("Client doesnt exist");
            } else if (!client.getAuthenticated()) {
                return new ResponseOrMessage<>("Client wasnt authenticated");
            }
            final List<Reservation> reservations = reservationRepository.findReservationsByClientAndStatus(client,  Constants.ReservationStatus.New);
            if (reservations == null) {
                return new ResponseOrMessage<>("Client does not have active reservations");
            } else {
                return new ResponseOrMessage<>(reservations);
            }
        } else {
            return new ResponseOrMessage<>("Wrong parameter");
        }
    }

    public ResponseOrMessage<List<Reservation>> getPayedReservations(@NotNull final Map<String, Long> clientId) {
        if (clientId.containsKey("clientId")) {
            final Client client = clientRepository.findById(clientId.get("clientId")).orElse(null);
            if (client == null) {
                return new ResponseOrMessage<>("Client doesnt exist");
            } else if (!client.getAuthenticated()) {
                return new ResponseOrMessage<>("Client wasnt authenticated");
            }
            final List<Reservation> reservations = reservationRepository.findReservationsByClientAndStatus(client,  Constants.ReservationStatus.Payed);
            if (reservations == null) {
                return new ResponseOrMessage<>("Client does not have payed reservations");
            } else {
                return new ResponseOrMessage<>(reservations);
            }

        } else {
            return new ResponseOrMessage<>("Wrong parameter");
        }
    }

    public ResponseOrMessage<List<Expo>> getNewExpos() {
        final List<Expo> expos = expoRepository.findExposByStatus(Constants.ExpoStatus.New);
        if (expos == null) {
            return new ResponseOrMessage<>("There are no new expos");
        } else {
            return new ResponseOrMessage<>(expos);
        }
    }

}
