package com.example.galleryservice.model.gallery;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class Reservation {

    private Long id;
    private Long client;
    private Double cost;
    private ReservationStatus status;

    @JsonFormat(pattern = "dd/MM/yyyy hh:mm")
    private LocalDateTime dateTime;

    private List<Ticket> tickets;

    public Reservation(@NotNull final Long client,
                       @NotNull final LocalDateTime dateTime,
                       @NotNull final List<Ticket> tickets) {
        this.client = client;
        this.tickets = tickets;
        this.cost = tickets.stream().map(Ticket::getCost).mapToDouble(Double::doubleValue).sum();
        this.status = ReservationStatus.New;
        this.dateTime = dateTime;
    }

    public boolean isNew() { return status.equals(ReservationStatus.New); }
    public boolean isPayed() { return status.equals(ReservationStatus.Payed); }
    public boolean isClosed() { return status.equals(ReservationStatus.Closed); }

}
