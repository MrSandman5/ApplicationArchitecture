package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.configuration.SpringContext;
import com.safonov.galleryservice.ArtGalleryApplication.data.StorageRepository;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Client;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @Column(name = "client")
    private Client client;

    @Min(value = 0, message = "must be greater than or equal to zero")
    @Column(name = "cost")
    private Double cost;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status")
    private ReservationStatus status;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "dateTime")
    private LocalDateTime dateTime;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private Set<Ticket> tickets;

    private final StorageRepository storageRepository = SpringContext.getBean(StorageRepository.class);

    public Reservation(@NotNull final Client client,
                       @NotNull final LocalDateTime dateTime,
                       @NotNull final Set<Ticket> tickets) {
        this.client = client;
        this.tickets = tickets;
        final Set<Ticket> resTickets = new HashSet<>();
        for (final Ticket ticket : tickets){
            resTickets.add(storageRepository.getTicket(ticket.getId()));
        }
        this.cost = resTickets.stream().map(Ticket::getCost).mapToDouble(Double::doubleValue).sum();
        this.status = ReservationStatus.New;
        this.dateTime = dateTime;
    }

    public boolean isNew() { return status.equals(ReservationStatus.New); }
    public boolean isPayed() { return status.equals(ReservationStatus.Payed); }
    public boolean isClosed() { return status.equals(ReservationStatus.Closed); }

}
