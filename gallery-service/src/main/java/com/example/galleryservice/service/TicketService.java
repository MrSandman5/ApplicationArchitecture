package com.example.galleryservice.service;

import com.example.galleryservice.model.project.Expo;
import com.example.galleryservice.model.project.Ticket;
import com.example.galleryservice.model.user.User;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface TicketService {

    List<Ticket> getAllTickets();

    List<Ticket> findTicketsByClient(@NotNull final User user);

    List<Ticket> findTicketsByExpo(@NotNull final Expo expo);

    Ticket findTicketById(final Long id);

    void deleteTicket(final Long id);

    void deleteTicketsByUser(@NotNull final User user);

    void deleteTicketsByExpo(@NotNull final Expo expo);

}
