package com.safonov.galleryservice.ArtGalleryApplication.entity.actor;

import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.ClientOwnerPayment;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Reservation;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Ticket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "client")
@EqualsAndHashCode(callSuper = true)
public class Client extends User{

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Reservation> reservations;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Ticket> tickets;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ClientOwnerPayment> clientOwnerPayments;

    public Client(@NotNull final String firstName,
                  @NotNull final String secondName) {
        this.firstName = firstName;
        this.lastName = secondName;
    }
}
