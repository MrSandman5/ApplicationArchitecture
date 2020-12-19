package com.safonov.galleryservice.ArtGalleryApplication.entity.actor;

import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.ClientOwnerPayment;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Reservation;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Ticket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "client")
@EqualsAndHashCode(callSuper = true)
public class Client extends User{

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClientOwnerPayment> clientOwnerPayments;

    public Client(@NotNull final String firstName,
                  @NotNull final String secondName) {
        this.firstName = firstName;
        this.lastName = secondName;
    }
}
