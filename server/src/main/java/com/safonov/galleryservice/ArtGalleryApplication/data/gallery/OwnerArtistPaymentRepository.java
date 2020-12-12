package com.safonov.galleryservice.ArtGalleryApplication.data.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.Expo;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.OwnerArtistPayment;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Artist;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Owner;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface OwnerArtistPaymentRepository extends PaymentRepository {

    Optional<OwnerArtistPayment> findPaymentByExpo(@NotNull final Expo expo);

    List<OwnerArtistPayment> findPaymentsByOwner(@NotNull final Owner owner);

    List<OwnerArtistPayment> findPaymentsByArtist(@NotNull final Artist artist);
}
