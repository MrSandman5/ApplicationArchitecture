package com.example.galleryservice.model.gallery;

import com.example.galleryservice.configuration.SpringContext;
import com.example.galleryservice.data.StorageDAO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Reservation {

    private long id;
    @NotNull
    private Long client;
    @Min(value = 0, message = "must be greater than or equal to zero")
    private double cost;
    @NotNull
    private ReservationStatus status;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateTime;

    @NotNull
    private List<Long> tickets;

    private final StorageDAO storageDAO = SpringContext.getBean(StorageDAO.class);

    public Reservation(@NotNull final Long client,
                       @NotNull final LocalDateTime dateTime,
                       @NotNull final List<Long> tickets) {
        this.client = client;
        this.tickets = tickets;
        final List<Ticket> resTickets = new ArrayList<>();
        for (Long ticket : tickets){
            resTickets.add(storageDAO.getTicket(ticket));
        }
        this.cost = resTickets.stream().map(Ticket::getCost).mapToDouble(Double::doubleValue).sum();
        this.status = ReservationStatus.New;
        this.dateTime = dateTime;
    }

    public boolean isNew() { return status.equals(ReservationStatus.New); }
    public boolean isPayed() { return status.equals(ReservationStatus.Payed); }
    public boolean isClosed() { return status.equals(ReservationStatus.Closed); }

}
