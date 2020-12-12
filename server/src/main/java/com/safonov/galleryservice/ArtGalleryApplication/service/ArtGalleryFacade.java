package com.safonov.galleryservice.ArtGalleryApplication.service;

import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.*;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Artist;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Client;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Owner;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public interface ArtGalleryFacade {

    void addUser(@NotNull final String login,
                 @NotNull final String password,
                 @NotNull final String name,
                 @NotNull final String email);
    /*void authenticate(@NotNull final String login,
                      @NotNull final String password);
    void signOut(@NotNull final String user);*/
    // get user information
    User getUser(final long user);
    User getUser(@NotNull final String user);
    // get client information
    List<Reservation> getClientReservations(@NotNull final String login);
    List<Ticket> getClientTickets(@NotNull final String login);
    // get owner information
    List<Expo> getOwnerExpos(@NotNull final String login);
    // get artist information
    List<Artwork> getArtistArtworks(@NotNull final String login);
    // get ticket information
    Ticket getTicket(final long ticket);
    // get reservation information
    Reservation getReservation(final long reservation);
    // get expo information
    Expo getExpo(final long expo);
    Expo getExpo(@NotNull final String expo);
    // get artwork information
    Artwork getArtwork(@NotNull final String artwork);
    //get payment information
    // client availabilities
    void addTicket(@NotNull final String user,
                   @NotNull final String expo);
    void createReservation(@NotNull final String user);
    double payForReservation(@NotNull final String client,
                             final long reservation,
                             @NotNull final String owner);
    // owner availabilities
    double acceptPayment(@NotNull final String owner, final long reservationId);
    public void createExpo(@NotNull final String owner,
                           @NotNull final String name,
                           @NotNull final String info,
                           @NotNull final Artist artist,
                           @NotNull final LocalDateTime startTime,
                           @NotNull final LocalDateTime endTime,
                           final double ticketPrice,
                           @NotNull final List<Artwork> artworks);
    void editExpo(@NotNull final String owner,
                  @NotNull final String expo,
                  @NotNull final String setting,
                  @NotNull final String data);
    void startExpo(@NotNull final String owner, @NotNull final String expo);
    void closeExpo(@NotNull final String owner, @NotNull final String expo);
    double payForExpo(@NotNull final String owner, @NotNull final String expo);
    // artist availabilities
    void addArtwork(@NotNull final String artist,
                    @NotNull final String name,
                    @NotNull final String info);
    double acceptRoyalties(@NotNull final String artist, @NotNull final String expo);
    // get content
    List<User> getAllUsers();
    List<Ticket> getAllTickets();
    List<Reservation> getAllReservations();
    List<Expo> getAllExpos();
    List<Artwork> getAllArtworks();
    List<Payment> getAllPayments();
    Client getClient(@NotNull final String client);
    Owner getOwner(@NotNull final String owner);
    Artist getArtist(@NotNull final String artist);
    Client getClient(final long client);
    Owner getOwner(final long owner);
    Artist getArtist(final long artist);
}
