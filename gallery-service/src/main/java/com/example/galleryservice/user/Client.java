package com.example.galleryservice.user;

import com.example.galleryservice.project.Reservation;
import com.example.galleryservice.project.Ticket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Client extends User {

    private final List<Reservation> reservations = new ArrayList<>();
    private final ClientStatus status;

    public Client(final Integer id,
                  @NotNull final String login,
                  @NotNull final String name,
                  @NotNull final String email,
                  @NotNull final ClientStatus status) {
        super(id, login, name, email);
        this.status = status;
    }

    public Client(@NotNull final User user, @NotNull final ClientStatus status) {
        super(user);
        this.status = status;
    }

    public void addReservation(@NotNull final Reservation reservation){
        reservations.add(reservation);
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void createReservation(@NotNull final List<Ticket> tickets){

    }

    public void payForReservation(@NotNull final Reservation reservation){

    }
}
