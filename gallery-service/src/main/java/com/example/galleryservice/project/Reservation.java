package com.example.galleryservice.project;

import com.example.galleryservice.user.Client;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Reservation {
    private Integer id;
    @NotNull
    private Client client;
    @NotNull
    private List<Ticket> tickets;
    private Integer cost;
    @NotNull
    private ReservationStatus status;

    public Reservation(final Integer id,
                       @NotNull final Client client,
                       @NotNull final List<Ticket> tickets) {
        this.id = id;
        this.client = client;
        this.tickets = tickets;
        for (Ticket ticket : tickets) {
            this.cost += ticket.getCost();
        }
        this.status = ReservationStatus.New;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(@NotNull final Client client) {
        this.client = client;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(@NotNull final List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(final int cost) {
        this.cost = cost;
    }

    public void addReservation(){
        status = ReservationStatus.WaitPay;
    }

    public void addCorpReservation(){
        status = ReservationStatus.WaitAccessManager;
    }

    public int applyDiscount(final int discount){
        if (!status.equals(ReservationStatus.WaitAccessManager)){
            return 1;
        }
        if (discount < 1 || discount > 50){
            return 2;
        }
        cost = cost * (100 - discount);
        status = ReservationStatus.WaitPay;
        return 0;
    }

}
