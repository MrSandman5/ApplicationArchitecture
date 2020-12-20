package com.safonov.galleryservice.ArtGalleryApplication.data.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.configuration.Constants;
import com.safonov.galleryservice.ArtGalleryApplication.data.BaseRepository;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Client;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Reservation;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface ReservationRepository extends BaseRepository<Reservation> {

    List<Reservation> findReservationsByClient(@NotNull final Client client);

    List<Reservation> findReservationsByStatus(@NotNull final Constants.ReservationStatus status);

    List<Reservation> findReservationsByClientAndStatus(@NotNull final Client client,
                                                        @NotNull final Constants.ReservationStatus status);
}
