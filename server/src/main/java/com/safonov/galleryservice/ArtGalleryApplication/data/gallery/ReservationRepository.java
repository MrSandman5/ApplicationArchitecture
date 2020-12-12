package com.safonov.galleryservice.ArtGalleryApplication.data.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.data.BaseRepository;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.Reservation;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.ReservationStatus;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Client;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface ReservationRepository extends BaseRepository<Reservation> {

    List<Reservation> findReservationsByClient(@NotNull final Client client);

    List<Reservation> findReservationsByStatus(@NotNull final ReservationStatus status);
}
