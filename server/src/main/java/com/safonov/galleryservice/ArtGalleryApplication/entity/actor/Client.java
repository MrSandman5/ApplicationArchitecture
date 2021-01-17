package com.safonov.galleryservice.ArtGalleryApplication.entity.actor;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.ClientOwnerPayment;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Reservation;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Ticket;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "client")
public class Client extends User{

    @JsonBackReference
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Reservation> reservations = new HashSet<>();

    @JsonBackReference
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Ticket> tickets = new HashSet<>();

    @JsonBackReference
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ClientOwnerPayment> clientOwnerPayments = new HashSet<>();

    public Client(@NotNull final String firstName,
                  @NotNull final String secondName) {
        this.firstName = firstName;
        this.lastName = secondName;
    }
}
