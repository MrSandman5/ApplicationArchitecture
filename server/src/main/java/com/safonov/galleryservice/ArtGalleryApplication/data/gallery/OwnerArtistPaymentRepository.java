package com.safonov.galleryservice.ArtGalleryApplication.data.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.data.BaseRepository;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Artist;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Owner;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Expo;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.OwnerArtistPayment;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface OwnerArtistPaymentRepository extends BaseRepository<OwnerArtistPayment> {

    Optional<OwnerArtistPayment> findPaymentByExpo(@NotNull final Expo expo);

    List<OwnerArtistPayment> findPaymentsByOwner(@NotNull final Owner owner);

    List<OwnerArtistPayment> findPaymentsByArtist(@NotNull final Artist artist);
}
