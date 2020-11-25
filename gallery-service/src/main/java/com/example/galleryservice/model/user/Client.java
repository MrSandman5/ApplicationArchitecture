package com.example.galleryservice.model.user;

import com.example.galleryservice.exceptions.*;
import com.example.galleryservice.model.gallery.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Client extends User {

    @NotNull
    private List<Long> reservations = new ArrayList<>();
    @NotNull
    private List<Long> tickets = new ArrayList<>();

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
        final Expo ticketExpo = getStorageDAO().getExpo(expo);
        if (ticketExpo == null){
            throw new ExpoNotFoundException(expo);
        }
        else if (ticketExpo.isClosed()){
            throw new ExpoAlreadyClosedException("Expo with id : " + ticketExpo.getId() + " already closed!");
        }
        if (!tickets.isEmpty()){
            for (Long tick : tickets) {
                if (ticketExpo.getId() != getStorageDAO().getTicket(tick).getExpo()){
                    throw new NotSameExpoException("Ticket's expo are not similar");
                }
            }
        }
        final Ticket newTicket = new Ticket(this.getId(), ticketExpo.getId(), ticketExpo.getTicketPrice());
        final long ticketId = getStorageDAO().addTicket(newTicket);
        final Ticket addedTicket = getStorageDAO().getTicket(ticketId);
        tickets.add(addedTicket.getId());
        getStorageDAO().updateClient(this);
        return addedTicket;
    }

    @SneakyThrows
    public Ticket addTicket(@NotNull final Expo expo){
        this.checkAuthentication();
        final Expo ticketExpo = getStorageDAO().getExpo(expo.getName());
        if (ticketExpo == null){
            throw new ExpoNotFoundException(expo.getName());
        }
        else if (ticketExpo.isClosed()){
            throw new ExpoAlreadyClosedException("Expo with id : " + ticketExpo.getId() + " already closed!");
        }
        if (!tickets.isEmpty()){
            for (Long tick : tickets) {
                if (ticketExpo.getId() != getStorageDAO().getTicket(tick).getExpo()){
                    throw new NotSameExpoException("Ticket's expo are not similar");
                }
            }
        }
        final Ticket newTicket = new Ticket(this.getId(), ticketExpo.getId(), ticketExpo.getTicketPrice());
        final long ticketId = getStorageDAO().addTicket(newTicket);
        final Ticket addedTicket = getStorageDAO().getTicket(ticketId);
        tickets.add(addedTicket.getId());
        getStorageDAO().updateClient(this);
        return addedTicket;
    }

    @SneakyThrows
    public Reservation createReservation(){
        this.checkAuthentication();
        final long expoId = getStorageDAO().getTicket(this.tickets.get(0)).getExpo();
        final Expo ticketExpo = getStorageDAO().getExpo(expoId);
        if (ticketExpo == null){
            throw new ExpoNotFoundException(expoId);
        }
        else if (ticketExpo.isClosed()){
            throw new ExpoAlreadyClosedException("This expo already closed!");
        }
        final Reservation reservation = new Reservation(this.getId(), ticketExpo.getStartTime(), this.getTickets());
        final long reservationId = getStorageDAO().addReservation(reservation);
        final Reservation addedReservation = getStorageDAO().getReservation(reservationId);
        this.reservations.add(addedReservation.getId());
        this.tickets.clear();
        getStorageDAO().updateClient(this);
        return addedReservation;
    }

    @SneakyThrows
    public double payForReservation(@NotNull final long reservation,
                                    @NotNull final Owner owner){
        this.checkAuthentication();
        final Reservation clientReservation = getStorageDAO().getReservation(reservation);
        if (clientReservation == null){
            throw new ReservationNotFoundException(reservation);
        }
        if (clientReservation.isClosed()){
            throw new ReservationAlreadyClosedException("Reservation with id : " + clientReservation.getId() + " already closed");
        }
        final long expoId = getStorageDAO().getTicket(clientReservation.getTickets().get(0)).getExpo();
        final Expo ticketExpo = getStorageDAO().getExpo(expoId);
        if (ticketExpo == null){
            throw new ExpoNotFoundException(expoId);
        } else if (ticketExpo.isClosed()){
            throw new ExpoAlreadyClosedException("Expo " + ticketExpo.getName() + " already closed");
        } else if (LocalDateTime.now().isAfter(ticketExpo.getStartTime())){
            this.reservations.remove(clientReservation.getId());
            getStorageDAO().updateClient(this);
            throw new ExpoAlreadyStartedException("Expo " + ticketExpo.getName() + " already started");
        }
        clientReservation.setStatus(ReservationStatus.Payed);
        getStorageDAO().updateReservation(clientReservation);
        final ClientOwnerPayment ticketPayment = new ClientOwnerPayment(reservation, this.getId(), owner.getId(), clientReservation.getCost());
        this.reservations.remove(clientReservation.getId());
        getStorageDAO().updateClient(this);
        final long clientOwnerPaymentId = getStorageDAO().addClientOwnerPayment(ticketPayment);
        return getStorageDAO().getPayment(clientOwnerPaymentId).getPrice();
    }

}
