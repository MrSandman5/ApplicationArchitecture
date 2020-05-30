package com.example.galleryservice.model.project;

import com.example.galleryservice.model.user.Client;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tickets")
    private List<Ticket> tickets;

    @Column(name = "cost")
    private Integer cost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;

    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "dateTime")
    private LocalDateTime dateTime;

    public Reservation(@NotNull final Client client,
                       @NotNull final List<Ticket> tickets,
                       @NotNull final LocalDateTime dateTime) {
        this.client = client;
        this.tickets = tickets;
        this.cost = tickets.stream().map(Ticket::getCost).mapToInt(Integer::intValue).sum();
        this.status = ReservationStatus.NEW;
        this.dateTime = dateTime;
    }

    public boolean isNew() {return status.equals(ReservationStatus.NEW);}
    public boolean isPayed() {return status.equals(ReservationStatus.PAYED);}
    public boolean isClosed() {return status.equals(ReservationStatus.CLOSED);}

}
