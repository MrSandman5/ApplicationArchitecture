package com.safonov.galleryservice.ArtGalleryApplication.model.user;

import com.safonov.galleryservice.ArtGalleryApplication.exceptions.*;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Client extends User {

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private final List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private final List<Ticket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "client_owner_payment", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private final List<ClientOwnerPayment> clientOwnerPayments = new ArrayList<>();

    public Client(@NotNull final String login,
                  @NotNull final String password,
                  @NotNull final String name,
                  @NotNull final String email) {
        super(login, password, name, email);
    }

    public Client(@NotNull final User user) {
        super(user);
    }

    @SneakyThrows
    public Ticket addTicket(@NotNull final String expo){
        this.checkAuthentication();
        final Expo ticketExpo = getStorageRepository().getExpo(expo);
        if (ticketExpo == null){
            throw new ExpoNotFoundException(expo);
        }
        else if (ticketExpo.isClosed()){
            throw new ExpoAlreadyClosedException("Expo with id : " + ticketExpo.getId() + " already closed!");
        }
        if (!tickets.isEmpty()){
            for (final Ticket tick : tickets) {
                if (!ticketExpo.getId().equals(getStorageRepository().getTicket(tick.getId()).getExpo().getId())){
                    throw new NotSameExpoException("Ticket's expo are not similar");
                }
            }
        }
        final Ticket newTicket = new Ticket(this, ticketExpo, ticketExpo.getTicketPrice());
        final long ticketId = getStorageRepository().addTicket(newTicket).getId();
        final Ticket addedTicket = getStorageRepository().getTicket(ticketId);
        tickets.add(addedTicket);
        getStorageRepository().updateClient(this);
        return addedTicket;
    }

    @SneakyThrows
    public Ticket addTicket(@NotNull final Expo expo){
        this.checkAuthentication();
        final Expo ticketExpo = getStorageRepository().getExpo(expo.getName());
        if (ticketExpo == null){
            throw new ExpoNotFoundException(expo.getName());
        }
        else if (ticketExpo.isClosed()){
            throw new ExpoAlreadyClosedException("Expo with id : " + ticketExpo.getId() + " already closed!");
        }
        if (!tickets.isEmpty()){
            for (final Ticket tick : tickets) {
                if (!ticketExpo.getId().equals(getStorageRepository().getTicket(tick.getId()).getExpo().getId())){
                    throw new NotSameExpoException("Ticket's expo are not similar");
                }
            }
        }
        final Ticket newTicket = new Ticket(this, ticketExpo, ticketExpo.getTicketPrice());
        final long ticketId = getStorageRepository().addTicket(newTicket).getId();
        final Ticket addedTicket = getStorageRepository().getTicket(ticketId);
        tickets.add(addedTicket);
        getStorageRepository().updateClient(this);
        return addedTicket;
    }

    @SneakyThrows
    public Reservation createReservation(){
        this.checkAuthentication();
        final long expoId = getStorageRepository().getTicket(this.tickets.get(0).getId()).getExpo().getId();
        final Expo ticketExpo = getStorageRepository().getExpo(expoId);
        if (ticketExpo == null){
            throw new ExpoNotFoundException(expoId);
        }
        else if (ticketExpo.isClosed()){
            throw new ExpoAlreadyClosedException("This expo already closed!");
        }
        final Reservation reservation = new Reservation(this, ticketExpo.getStartTime(), this.getTickets());
        final long reservationId = getStorageRepository().addReservation(reservation).getId();
        final Reservation addedReservation = getStorageRepository().getReservation(reservationId);
        this.reservations.add(addedReservation);
        this.tickets.clear();
        getStorageRepository().updateClient(this);
        return addedReservation;
    }

    @SneakyThrows
    public double payForReservation(@NotNull final long reservation,
                                    @NotNull final Owner owner){
        this.checkAuthentication();
        final Reservation clientReservation = getStorageRepository().getReservation(reservation);
        if (clientReservation == null){
            throw new ReservationNotFoundException(reservation);
        }
        if (clientReservation.isClosed()){
            throw new ReservationAlreadyClosedException("Reservation with id : " + clientReservation.getId() + " already closed");
        }
        final long expoId = getStorageRepository().getTicket(clientReservation.getTickets().get(0).getId()).getExpo().getId();
        final Expo ticketExpo = getStorageRepository().getExpo(expoId);
        if (ticketExpo == null){
            throw new ExpoNotFoundException(expoId);
        } else if (ticketExpo.isClosed()){
            throw new ExpoAlreadyClosedException("Expo " + ticketExpo.getName() + " already closed");
        } else if (LocalDateTime.now().isAfter(ticketExpo.getStartTime())){
            this.reservations.remove(clientReservation);
            getStorageRepository().updateClient(this);
            throw new ExpoAlreadyStartedException("Expo " + ticketExpo.getName() + " already started");
        }
        clientReservation.setStatus(ReservationStatus.Payed);
        getStorageRepository().updateReservation(clientReservation);
        final ClientOwnerPayment ticketPayment = new ClientOwnerPayment(clientReservation, this, owner, clientReservation.getCost());
        this.reservations.remove(clientReservation);
        getStorageRepository().updateClient(this);
        final long clientOwnerPaymentId = getStorageRepository().addClientOwnerPayment(ticketPayment);
        return getStorageRepository().getPayment(clientOwnerPaymentId).getPrice();
    }

}
