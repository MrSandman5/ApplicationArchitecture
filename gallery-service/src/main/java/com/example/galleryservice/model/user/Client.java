package com.example.galleryservice.model.user;

import com.example.galleryservice.data.StorageDAO;
import com.example.galleryservice.exceptions.*;
import com.example.galleryservice.model.project.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Client extends User {

    private final List<Reservation> reservations = new ArrayList<>();
    private final List<Ticket> tickets = new ArrayList<>();

    private final StorageDAO storageDAO = new StorageDAO();

    public Client(@NotNull final String login,
                  @NotNull final String password,
                  @NotNull final String name,
                  @NotNull final String email) {
        super(login, password, name, email, UserRole.Client);
    }

    public Client(@NotNull final User user) {
        super(user);
    }

    @SneakyThrows
    public void addTicket(@NotNull final Ticket ticket){
        this.checkAuthentication();
        final Expo ticketExpo = storageDAO.getExpo(ticket.getExpo());
        if (ticketExpo == null){
            throw new ExpoNotFoundException(ticket.getExpo());
        }
        else if (ticketExpo.isClosed()){
            throw new ExpoAlreadyClosedException("Expo with id : " + ticketExpo.getId() + " already closed!");
        }
        if (!tickets.isEmpty()){
            for (Ticket tick : tickets) {
                if (ticket.getExpo() != tick.getExpo()){
                    throw new NotSameExpoException("Ticket's expo are not similar");
                }
            }
        }
        tickets.add(ticket);
        storageDAO.addTicket(ticket);
    }

    @SneakyThrows
    public Reservation createReservation(){
        this.checkAuthentication();
        final Expo ticketExpo = storageDAO.getExpo(this.tickets.get(0).getExpo());
        if (ticketExpo == null){
            throw new ExpoNotFoundException(this.tickets.get(0).getExpo());
        }
        else if (ticketExpo.isClosed()){
            throw new ExpoAlreadyClosedException("This expo already closed!");
        }
        final Reservation reservation = new Reservation(this.getId(), ticketExpo.getStartTime(), this.getTickets());
        for (Reservation entry : reservations){
            if (entry.equals(reservation)){
                throw new ReservationAlreadyExistedException("This reservation already existed for this user!");
            }
        }
        this.reservations.add(reservation);
        storageDAO.addReservation(reservation);
        this.tickets.clear();
        return reservation;
    }

    @SneakyThrows
    public double payForReservation(@NotNull final Reservation reservation,
                                    @NotNull final Owner owner){
        this.checkAuthentication();
        final Reservation clientReservation = storageDAO.getReservation(reservation.getId());
        if (clientReservation == null){
            throw new ReservationNotFoundException(reservation.getId());
        }
        if (clientReservation.isClosed()){
            throw new ReservationAlreadyClosedException("Reservation with id : " + clientReservation.getId() + " already closed");
        } else if (this.reservations.contains(clientReservation)){
            throw new ReservationAlreadyExistedException("Reservation with id : " + clientReservation.getId() + " already existed for this client");
        }
        final Expo ticketExpo = storageDAO.getExpo(clientReservation.getTickets().get(0).getExpo());
        if (ticketExpo == null){
            throw new ExpoNotFoundException(clientReservation.getTickets().get(0).getExpo());
        } else if (ticketExpo.isOpened()
                || LocalDateTime.now().until(ticketExpo.getStartTime(), ChronoUnit.MINUTES) < 30) {
            //return buyTickets(clientReservation);
        } else if (ticketExpo.isClosed()){
            throw new ExpoAlreadyClosedException("Expo " + ticketExpo.getName() + " already closed");
        }
        final double payment = clientReservation.getTickets().stream()
                .map(Ticket::getCost)
                .mapToDouble(Double::doubleValue)
                .sum();

        clientReservation.setStatus(ReservationStatus.Payed);
        storageDAO.updateReservation(clientReservation);
        final ClientOwnerPayment ticketPayment = new ClientOwnerPayment(clientReservation.getId(), this.getId(), owner.getId(), payment);
        storageDAO.addClientOwnerPayment(ticketPayment);
        return ticketPayment.getAmount();
    }

}
