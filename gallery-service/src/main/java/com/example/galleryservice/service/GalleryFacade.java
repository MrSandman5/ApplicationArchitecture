package com.example.galleryservice.service;

import com.example.galleryservice.model.gallery.Ticket;
import com.example.galleryservice.model.user.UserRole;

import javax.validation.constraints.NotNull;

public interface GalleryFacade {
    void addUser(@NotNull final String login,
                 @NotNull final String password,
                 @NotNull final String name,
                 @NotNull final String email) throws Exception;
    void authenticate(@NotNull final String login,
                      @NotNull final String password) throws Exception;
    void signOut(@NotNull final String login) throws Exception;
    // get user information
    String getUserEmail(@NotNull final String login) throws Exception;
    String getUserName(@NotNull final String login) throws Exception;
    UserRole getUserRole(@NotNull final String login) throws Exception;
    // client availabilities
    void addTicket(@NotNull final String login, final long ticketId) throws Exception;
    void createReservation(@NotNull final String login) throws Exception;
    void payForReservation(@NotNull final String client,
                           final long reservationId,
                           @NotNull final String owner);
    // owner availabilities
    void acceptPayment(@NotNull final String login, final long reservationId);

}
