package com.safonov.galleryservice.ArtGalleryApplication.data.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.data.BaseRepository;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Client;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Owner;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.ClientOwnerPayment;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Reservation;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientOwnerPaymentRepository extends BaseRepository<ClientOwnerPayment> {

    Optional<ClientOwnerPayment> findPaymentByReservation(@NotNull final Reservation reservation);

    List<ClientOwnerPayment> findPaymentsByClient(@NotNull final Client client);

    List<ClientOwnerPayment> findPaymentsByOwner(@NotNull final Owner owner);
}
