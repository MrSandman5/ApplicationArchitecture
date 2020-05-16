package com.example.galleryservice.user;

import com.example.galleryservice.exceptions.ExpoAlreadyClosedException;
import com.example.galleryservice.exceptions.ReservationAlreadyClosedException;
import com.example.galleryservice.project.Reservation;
import com.example.galleryservice.project.Ticket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Client extends User {

    @NotNull
    private final PrivateAccount privateAccount;
    private final List<Reservation> reservations = new ArrayList<>();
    private List<Ticket> tickets = new ArrayList<>();

    public Client(final Integer id,
                  @NotNull final String login,
                  @NotNull final String name,
                  @NotNull final String email,
                  @NotNull final PrivateAccount privateAccount) {
        super(id, login, name, email);
        this.privateAccount = privateAccount;
    }

    public Client(@NotNull final User user, @NotNull final PrivateAccount privateAccount) {
        super(user);
        this.privateAccount = privateAccount;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    @SneakyThrows
    public void addTicket(@NotNull final Ticket ticket){
        if (ticket.getExpo().isClosed()){
            throw new ExpoAlreadyClosedException(ticket.getExpo().getName() + "is already closed!");
        }
        checkAuthentication();
        tickets.add(ticket);
    }

    @SneakyThrows
    public Reservation createReservation(){
        checkAuthentication();
        final Reservation res = new Reservation(this, this.tickets);
        reservations.add(res);
        this.tickets.clear();
        return res;
    }

    @SneakyThrows
    public void payForReservation(@NotNull final Reservation reservation,
                                  @NotNull final Owner owner){
        if (reservation.isClosed()){
            throw new ReservationAlreadyClosedException(reservation.getId() + "is already closed!");
        }
        checkAuthentication();
        this.privateAccount.transfer(reservation.getTickets().get(0).getExpo(), reservation.getCost(), owner.getCorporateAccount());
        reservation.setPayed();
        this.tickets = owner.closeReservation(reservation);
        reservations.remove(reservation);
    }

    @SneakyThrows
    public void buyTickets(@NotNull final Owner owner){
        checkAuthentication();
        final int cost = this.tickets.stream().map(Ticket::getCost).mapToInt(Integer::intValue).sum();
        this.privateAccount.transfer(tickets.get(0).getExpo(), cost, owner.getCorporateAccount());
    }
}
