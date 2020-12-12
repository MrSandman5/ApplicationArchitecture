package com.safonov.galleryservice.ArtGalleryApplication.data.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.data.BaseRepository;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.Expo;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.Reservation;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.Ticket;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface TicketRepository extends BaseRepository<Ticket> {

    List<Ticket> findTicketsByReservation(@NotNull final Reservation reservation);

    List<Ticket> findTicketsByExpo(@NotNull final Expo expo);
}
