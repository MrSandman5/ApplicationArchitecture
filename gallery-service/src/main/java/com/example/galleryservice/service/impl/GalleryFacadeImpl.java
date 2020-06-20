package com.example.galleryservice.service.impl;

import com.example.galleryservice.data.StorageDAO;
import com.example.galleryservice.model.gallery.*;
import com.example.galleryservice.model.user.*;
import com.example.galleryservice.service.GalleryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class GalleryFacadeImpl implements GalleryFacade {

    private final StorageDAO storageDAO;

    @Autowired
    public GalleryFacadeImpl(@NotNull final StorageDAO storageDAO){
        this.storageDAO = storageDAO;
    }

    @Override
    public void addUser(@NotNull final String login,
                        @NotNull final String password,
                        @NotNull final String name,
                        @NotNull final String email) {
        storageDAO.addUser(login, password, name, email);
    }

    @Override
    public void authenticate(@NotNull final String login,
                             @NotNull final String password) {
        storageDAO.authenticate(login, password);
    }

    @Override
    public void signOut(@NotNull final String user) {
        storageDAO.signOut(user);
    }

    @Override
    public User getUser(final long user){
        return storageDAO.getUser(user);
    }

    @Override
    public User getUser(@NotNull final String user){
        return storageDAO.getUser(user);
    }

    @Override
    public List<Reservation> getClientReservations(@NotNull final String client) {
        final List<Reservation> reservations = new ArrayList<>();
        for (Long id : getClient(client).getReservations()){
            reservations.add(getReservation(id));
        }
        return reservations;
    }

    @Override
    public List<Ticket> getClientTickets(@NotNull final String client) {
        final List<Ticket> tickets = new ArrayList<>();
        for (Long id : getClient(client).getTickets()){
            tickets.add(getTicket(id));
        }
        return tickets;
    }

    @Override
    public List<Expo> getOwnerExpos(@NotNull final String owner) {
        final List<Expo> expos = new ArrayList<>();
        for (String name : getOwner(owner).getExpos()){
            expos.add(getExpo(name));
        }
        return expos;
    }

    @Override
    public List<Artwork> getArtistArtworks(@NotNull final String artist) {
        final List<Artwork> artworks = new ArrayList<>();
        for (String name : getArtist(artist).getArtworks()){
            artworks.add(getArtwork(name));
        }
        return artworks;
    }

    @Override
    public Ticket getTicket(final long ticket) {
        return storageDAO.getTicket(ticket);
    }

    @Override
    public Reservation getReservation(final long reservation) {
        return storageDAO.getReservation(reservation);
    }

    @Override
    public Expo getExpo(final long expo) {
        return storageDAO.getExpo(expo);
    }

    @Override
    public Expo getExpo(@NotNull final String expo) {
        return storageDAO.getExpo(expo);
    }

    @Override
    public Artwork getArtwork(@NotNull final String artwork) {
        return storageDAO.getArtwork(artwork);
    }

    @Override
    public Payment getPayment(final long payment) {
        return storageDAO.getPayment(payment);
    }

    @Override
    public ClientOwnerPayment getClientOwnerPayment(final long payment) {
        return storageDAO.getClientOwnerPayment(payment);
    }

    @Override
    public OwnerArtistPayment getOwnerArtistPayment(final long payment) {
        return storageDAO.getOwnerArtistPayment(payment);
    }

    @Override
    public void addTicket(@NotNull final String client,
                            @NotNull final String expo) {
        final Client targetClient = storageDAO.getClient(client);
        targetClient.addTicket(expo);
    }

    @Override
    public void createReservation(@NotNull final String client) {
        final Client targetClient = storageDAO.getClient(client);
        targetClient.createReservation();
    }

    @Override
    public double payForReservation(@NotNull final String client,
                                  final long reservation,
                                  @NotNull final String owner) {
        final Client targetClient = storageDAO.getClient(client);
        final Owner targetOwner = storageDAO.getOwner(owner);
        return targetClient.payForReservation(reservation, targetOwner);
    }

    @Override
    public double acceptPayment(@NotNull final String owner, final long reservationId) {
        final Owner targetOwner = storageDAO.getOwner(owner);
        return targetOwner.acceptPayment(reservationId);
    }

    @Override
    public void createExpo(@NotNull final String owner,
                           @NotNull final String name,
                           @NotNull final String info,
                           final long artist,
                           @NotNull final LocalDateTime startTime,
                           @NotNull final LocalDateTime endTime,
                           final double ticketPrice,
                           @NotNull final List<String> artworks) {
        final Owner targetOwner = storageDAO.getOwner(owner);
        targetOwner.createExpo(name, info, artist, startTime, endTime, ticketPrice, artworks);
    }

    @Override
    public void editExpo(@NotNull final String owner,
                         @NotNull final String expo,
                         @NotNull final String setting,
                         @NotNull final String data) {
        final Owner targetOwner = storageDAO.getOwner(owner);
        targetOwner.editExpo(expo, EditSettings.valueOf(setting), data);
    }

    @Override
    public void startExpo(@NotNull final String owner, @NotNull final String expo) {
        final Owner targetOwner = storageDAO.getOwner(owner);
        targetOwner.startExpo(expo);
    }

    @Override
    public void closeExpo(@NotNull final String owner, @NotNull final String expo) {
        final Owner targetOwner = storageDAO.getOwner(owner);
        targetOwner.closeExpo(expo);
    }

    @Override
    public double payForExpo(@NotNull final String owner, @NotNull final String expo) {
        final Owner targetOwner = storageDAO.getOwner(owner);
        return targetOwner.payForExpo(expo);
    }

    @Override
    public void addArtwork(@NotNull final String artist,
                           @NotNull final String name,
                           @NotNull final String info) {
        final Artist targetArtist = storageDAO.getArtist(artist);
        targetArtist.addArtwork(name, info);
    }

    @Override
    public double acceptRoyalties(@NotNull final String artist,
                                  @NotNull final String expo) {
        final Artist targetArtist = storageDAO.getArtist(artist);
        return targetArtist.acceptRoyalties(expo);
    }

    @Override
    public List<User> getAllUsers() {
        return storageDAO.getUserDAO().findAll();
    }

    @Override
    public List<Ticket> getAllTickets() {
        return storageDAO.getTicketDAO().findAll();
    }

    @Override
    public List<Reservation> getAllReservations() {
        return storageDAO.getReservationDAO().findAll();
    }

    @Override
    public List<Expo> getAllExpos() {
        return storageDAO.getExpoDAO().findAll();
    }

    @Override
    public List<Artwork> getAllArtworks() {
        return storageDAO.getArtworkDAO().findAll();
    }

    @Override
    public List<Payment> getAllPayments() {
        return storageDAO.getPaymentDAO().findAll();
    }

    @Override
    public Client getClient(@NotNull final String client) {
        return storageDAO.getClient(client);
    }

    @Override
    public Owner getOwner(@NotNull final String owner) {
        return storageDAO.getOwner(owner);
    }

    @Override
    public Artist getArtist(@NotNull final String artist) {
        return storageDAO.getArtist(artist);
    }

    @Override
    public Client getClient(final long client) {
        return storageDAO.getClient(client);
    }

    @Override
    public Owner getOwner(final long owner) {
        return storageDAO.getOwner(owner);
    }

    @Override
    public Artist getArtist(final long artist) {
        return storageDAO.getArtist(artist);
    }
}
