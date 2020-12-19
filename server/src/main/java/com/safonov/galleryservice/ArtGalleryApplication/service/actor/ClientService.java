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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public ApiResponse createReservation(@NotNull final CreateReservationModel model){
        final Client client = clientRepository.findById(model.getClientId()).orElse(null);
        if (client == null) {
            return new ApiResponse("Client doesnt exist");
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

}
