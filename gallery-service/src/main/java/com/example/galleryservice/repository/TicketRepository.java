package com.example.galleryservice.repository;

import com.example.galleryservice.model.project.Expo;
import com.example.galleryservice.model.project.Ticket;
import com.example.galleryservice.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findTicketsByClient(@NotNull final User user);

    List<Ticket> findTicketsByExpo(@NotNull final Expo expo);

    void deleteTicketsByClient(@NotNull final User user);

    void deleteTicketsByExpo(@NotNull final Expo Expo);
}
