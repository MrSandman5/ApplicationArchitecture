package com.example.galleryservice.service;

import com.example.galleryservice.model.project.Reservation;
import com.example.galleryservice.model.user.User;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ReservationService {

    List<Reservation> getAllReservations();

    List<Reservation> findNewReservations();

    List<Reservation> findPayedReservations();

    List<Reservation> findClosedReservations();

    List<Reservation> findReservationsByClient(@NotNull final User user);

    Reservation findReservationById(final Long id);

    void deleteReservation(final Long id);

    void deleteReservationsByClient(@NotNull final User user);
}
