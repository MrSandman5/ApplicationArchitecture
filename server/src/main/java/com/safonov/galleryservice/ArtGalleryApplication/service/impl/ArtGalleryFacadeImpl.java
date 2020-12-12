package com.safonov.galleryservice.ArtGalleryApplication.service.impl;

import com.safonov.galleryservice.ArtGalleryApplication.data.StorageRepository;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.*;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Artist;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Client;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Owner;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.User;
import com.safonov.galleryservice.ArtGalleryApplication.service.ArtGalleryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ArtGalleryFacadeImpl implements ArtGalleryFacade {

    private final StorageRepository storageRepository;

    @Autowired
    public ArtGalleryFacadeImpl(@NotNull final StorageRepository storageRepository){
        this.storageRepository = storageRepository;
    }

    @Override
    public void addUser(@NotNull final String login,
                        @NotNull final String password,
                        @NotNull final String name,
                        @NotNull final String email) {
        storageRepository.addUser(login, password, name, email);
    }

    /*@Override
    public void authenticate(@NotNull final String login,
                             @NotNull final String password) {
        storageRepository.authenticate(login, password);
    }

    @Override
    public void signOut(@NotNull final String user) {
        storageRepository.signOut(user);
    }*/

    @Override
    public User getUser(final long user){
        return storageRepository.getUser(user);
    }

    @Override
    public User getUser(@NotNull final String user){
        return storageRepository.getUser(user);
    }

    @Override
    public List<Reservation> getClientReservations(@NotNull final String client) {
        final List<Reservation> reservations = new ArrayList<>();
        for (final Reservation reservation : getClient(client).getReservations()){
            reservations.add(getReservation(reservation.getId()));
        }
        return reservations;
    }

    @Override
    public List<Ticket> getClientTickets(@NotNull final String client) {
        final List<Ticket> tickets = new ArrayList<>();
        for (final Ticket ticket : getClient(client).getTickets()){
            tickets.add(getTicket(ticket.getId()));
        }
        return tickets;
    }

    @Override
    public List<Expo> getOwnerExpos(@NotNull final String owner) {
        final List<Expo> expos = new ArrayList<>();
        for (final Expo expo : getOwner(owner).getExpos()){
            expos.add(getExpo(expo.getName()));
        }
        return expos;
    }

    @Override
    public List<Artwork> getArtistArtworks(@NotNull final String artist) {
        final List<Artwork> artworks = new ArrayList<>();
        for (final Artwork artwork : getArtist(artist).getArtworks()){
            artworks.add(getArtwork(artwork.getName()));
        }
        return artworks;
    }

    @Override
    public Ticket getTicket(final long ticket) {
        return storageRepository.getTicket(ticket);
    }

    @Override
    public Reservation getReservation(final long reservation) {
        return storageRepository.getReservation(reservation);
    }

    @Override
    public Expo getExpo(final long expo) {
        return storageRepository.getExpo(expo);
    }

    @Override
    public Expo getExpo(@NotNull final String expo) {
        return storageRepository.getExpo(expo);
    }

    @Override
    public Artwork getArtwork(@NotNull final String artwork) {
        return storageRepository.getArtwork(artwork);
    }

    @Override
    public void addTicket(@NotNull final String client,
                          @NotNull final String expo) {
        final Client targetClient = storageRepository.getClient(client);
        targetClient.addTicket(expo);
    }

    @Override
    public void createReservation(@NotNull final String client) {
        final Client targetClient = storageRepository.getClient(client);
        targetClient.createReservation();
    }

    @Override
    public double payForReservation(@NotNull final String client,
                                    final long reservation,
                                    @NotNull final String owner) {
        final Client targetClient = storageRepository.getClient(client);
        final Owner targetOwner = storageRepository.getOwner(owner);
        return targetClient.payForReservation(reservation, targetOwner);
    }

    @Override
    public double acceptPayment(@NotNull final String owner, final long reservationId) {
        final Owner targetOwner = storageRepository.getOwner(owner);
        return targetOwner.acceptPayment(reservationId);
    }

    @Override
    public void createExpo(@NotNull final String owner,
                           @NotNull final String name,
                           @NotNull final String info,
                           @NotNull final Artist artist,
                           @NotNull final LocalDateTime startTime,
                           @NotNull final LocalDateTime endTime,
                           final double ticketPrice,
                           @NotNull final List<Artwork> artworks) {
        final Owner targetOwner = storageRepository.getOwner(owner);
        targetOwner.createExpo(name, info, artist, startTime, endTime, ticketPrice, artworks);
    }

    @Override
    public void editExpo(@NotNull final String owner,
                         @NotNull final String expo,
                         @NotNull final String setting,
                         @NotNull final String data) {
        final Owner targetOwner = storageRepository.getOwner(owner);
        targetOwner.editExpo(expo, EditSettings.valueOf(setting), data);
    }

    @Override
    public void startExpo(@NotNull final String owner, @NotNull final String expo) {
        final Owner targetOwner = storageRepository.getOwner(owner);
        targetOwner.startExpo(expo);
    }

    @Override
    public void closeExpo(@NotNull final String owner, @NotNull final String expo) {
        final Owner targetOwner = storageRepository.getOwner(owner);
        targetOwner.closeExpo(expo);
    }

    @Override
    public double payForExpo(@NotNull final String owner, @NotNull final String expo) {
        final Owner targetOwner = storageRepository.getOwner(owner);
        return targetOwner.payForExpo(expo);
    }

    @Override
    public void addArtwork(@NotNull final String artist,
                           @NotNull final String name,
                           @NotNull final String info) {
        final Artist targetArtist = storageRepository.getArtist(artist);
        targetArtist.addArtwork(name, info);
    }

    @Override
    public double acceptRoyalties(@NotNull final String artist,
                                  @NotNull final String expo) {
        final Artist targetArtist = storageRepository.getArtist(artist);
        return targetArtist.acceptRoyalties(expo);
    }

    @Override
    public List<User> getAllUsers() {
        return storageRepository.getUserRepository().findAll();
    }

    @Override
    public List<Ticket> getAllTickets() {
        return storageRepository.getTicketRepository().findAll();
    }

    @Override
    public List<Reservation> getAllReservations() {
        return storageRepository.getReservationRepository().findAll();
    }

    @Override
    public List<Expo> getAllExpos() {
        return storageRepository.getExpoRepository().findAll();
    }

    @Override
    public List<Artwork> getAllArtworks() {
        return storageRepository.getArtworkRepository().findAll();
    }

    @Override
    public List<Payment> getAllPayments() {
        return storageRepository.getPaymentRepository().findAll();
    }

    @Override
    public Client getClient(@NotNull final String client) {
        return storageRepository.getClient(client);
    }

    @Override
    public Owner getOwner(@NotNull final String owner) {
        return storageRepository.getOwner(owner);
    }

    @Override
    public Artist getArtist(@NotNull final String artist) {
        return storageRepository.getArtist(artist);
    }

    @Override
    public Client getClient(final long client) {
        return storageRepository.getClient(client);
    }

    @Override
    public Owner getOwner(final long owner) {
        return storageRepository.getOwner(owner);
    }

    @Override
    public Artist getArtist(final long artist) {
        return storageRepository.getArtist(artist);
    }
}
