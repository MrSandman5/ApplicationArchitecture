package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ticket")
@Data
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @Column(name = "reservation")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @Column(name = "expo")
    private Expo expo;

    @Min(value = 0, message = "must be greater than or equal to zero")
    @Column(name = "cost")
    private Double cost;

    public Ticket(@NotNull final Reservation reservation,
                  @NotNull final Expo expo,
                  final Double cost) {
        this.reservation = reservation;
        this.expo = expo;
        this.cost = cost;
    }

}

