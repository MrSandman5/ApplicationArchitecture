package com.example.galleryservice.repository;

import com.example.galleryservice.model.project.Reservation;
import com.example.galleryservice.model.project.ReservationStatus;
import com.example.galleryservice.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findReservationsByClient(@NotNull final User user);

    List<Reservation> findReservationsByStatus(@NotNull final ReservationStatus status);

    void deleteReservationsByClient(@NotNull final User user);

    void deleteReservationsByStatus(@NotNull final ReservationStatus status);

}
