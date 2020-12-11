package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.model.user.Client;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Owner;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "client_owner_payment")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ClientOwnerPayment extends Payment {

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @NotNull
    @Column(name = "reservation")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @Column(name = "client")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @Column(name = "owner")
    private Owner owner;

    public ClientOwnerPayment(@NotNull final Reservation reservation,
                              @NotNull final Client client,
                              @NotNull final Owner owner,
                              final double amount){
        super(amount);
        this.reservation = reservation;
        this.client = client;
        this.owner = owner;
    }

}
