package com.safonov.galleryservice.ArtGalleryApplication.entity.gallery;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.safonov.galleryservice.ArtGalleryApplication.entity.AbstractEntity;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@Table(name = "ticket")
@EqualsAndHashCode(callSuper = true)
public class Ticket extends AbstractEntity {

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "expo_id", nullable = false)
    private Expo expo;

    @Min(value = 0, message = "must be greater than or equal to zero")
    @Column(name = "cost", nullable = false)
    private Double cost;

    public Ticket(@NotNull final Client client,
                  @NotNull final Expo expo,
                  final Double cost) {
        this.client = client;
        this.expo = expo;
        this.cost = cost;
    }
}
