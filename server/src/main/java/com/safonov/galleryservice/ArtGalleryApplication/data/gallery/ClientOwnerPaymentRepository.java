package com.safonov.galleryservice.ArtGalleryApplication.data.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.ClientOwnerPayment;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.Reservation;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Client;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Owner;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientOwnerPaymentRepository extends PaymentRepository {

    Optional<ClientOwnerPayment> findPaymentByReservation(@NotNull final Reservation reservation);

    List<ClientOwnerPayment> findPaymentsByClient(@NotNull final Client client);

    List<ClientOwnerPayment> findPaymentsByOwner(@NotNull final Owner owner);
}
