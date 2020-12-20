package com.safonov.galleryservice.ArtGalleryApplication.data.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.data.BaseRepository;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Client;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Expo;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Reservation;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Ticket;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface TicketRepository extends BaseRepository<Ticket> {

    List<Ticket> findTicketsByClient(@NotNull final Client client);

    List<Ticket> findTicketsByReservation(@NotNull final Reservation reservation);

    List<Ticket> findTicketsByExpo(@NotNull final Expo expo);
}
