package com.example.galleryservice.project;

import com.example.galleryservice.user.Client;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Reservation {

    public enum ReservationStatus {
        NEW,
        PAYED,
        CLOSED
    }

    private Integer id;
    @NotNull
    private Client client;
    @NotNull
    private List<Ticket> tickets;
    private Integer cost;
    @NotNull
    private ReservationStatus status;
    @NotNull
    private final LocalDateTime dateTime;

    public Reservation(@NotNull Client client, @NotNull List<Ticket> tickets) {
        this.id = -1;
        this.client = client;
        this.tickets = tickets;
        this.cost = tickets.stream().map(Ticket::getCost).mapToInt(Integer::intValue).sum();
        this.status = ReservationStatus.NEW;
        this.dateTime = LocalDateTime.now();
    }

    public Reservation(final Integer id,
                       @NotNull final Client client,
                       @NotNull final List<Ticket> tickets,
                       @NotNull final LocalDateTime dateTime) {
        this.id = id;
        this.client = client;
        this.tickets = tickets;
        this.cost = tickets.stream().map(Ticket::getCost).mapToInt(Integer::intValue).sum();
        this.status = ReservationStatus.NEW;
        this.dateTime = dateTime;
    }

    public Integer getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public Integer getCost() {
        return cost;
    }

    public ReservationStatus getStatus(){
        return status;
    }

    public boolean isNew() {return status.equals(ReservationStatus.NEW);}
    public boolean isPayed() {return status.equals(ReservationStatus.PAYED);}
    public boolean isClosed() {return status.equals(ReservationStatus.CLOSED);}

    public void setPayed(){
        this.status = ReservationStatus.PAYED;
    }

    public void setClosed(){
        this.status = ReservationStatus.CLOSED;
    }

}
