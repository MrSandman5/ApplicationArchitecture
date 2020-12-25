package com.safonov.galleryservice.ArtGalleryApplication.entity.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.configuration.Constants;
import com.safonov.galleryservice.ArtGalleryApplication.entity.AbstractEntity;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Client;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "reservation")
@EqualsAndHashCode(callSuper = true)
public class Reservation extends AbstractEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Constants.ReservationStatus status;

    @Min(value = 0, message = "must be greater than or equal to zero")
    @Column(name = "cost", nullable = false)
    private Double cost;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "dateTime", nullable = false)
    private LocalDateTime dateTime;

    @OneToMany(mappedBy = "reservation",fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Ticket> tickets;

    public Reservation(@NotNull final Client client,
                       @NotNull final LocalDateTime dateTime) {
        this.client = client;
        this.tickets = client.getTickets();
        this.cost = this.tickets.stream()
                .map(Ticket::getCost)
                .mapToDouble(Double::doubleValue)
                .sum();
        this.status = Constants.ReservationStatus.New;
        this.dateTime = dateTime;
    }

    public boolean isNew() { return status.equals(Constants.ReservationStatus.New); }
    public boolean isPayed() { return status.equals(Constants.ReservationStatus.Payed); }
    public boolean isClosed() { return status.equals(Constants.ReservationStatus.Closed); }
}
