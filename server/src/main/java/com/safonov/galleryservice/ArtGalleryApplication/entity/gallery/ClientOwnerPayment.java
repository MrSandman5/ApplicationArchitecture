package com.safonov.galleryservice.ArtGalleryApplication.entity.gallery;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Client;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Owner;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "client_owner_payment")
public class ClientOwnerPayment extends Payment{

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    public ClientOwnerPayment(@NotNull final Reservation reservation,
                              @NotNull final Client client,
                              @NotNull final Owner owner) {
        super(reservation.getCost());
        this.reservation = reservation;
        this.client = client;
        this.owner = owner;
    }
}
