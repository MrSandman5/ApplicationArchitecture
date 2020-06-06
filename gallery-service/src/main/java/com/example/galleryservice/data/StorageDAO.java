package com.example.galleryservice.data;

import com.example.galleryservice.data.project.*;
import com.example.galleryservice.data.user.UserDAO;
import com.example.galleryservice.exceptions.*;
import com.example.galleryservice.model.project.*;
import com.example.galleryservice.model.user.*;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
@Data
public class StorageDAO {

    private final UserDAO userDAO;
    private final TicketDAO ticketDAO;
    private final ReservationDAO reservationDAO;
    private final ExpoDAO expoDAO;
    private final ArtworkDAO artworkDAO;
    private final ClientOwnerPaymentDAO clientOwnerPaymentDAO;
    private final OwnerArtistPaymentDAO ownerArtistPaymentDAO;

    @Autowired
    public StorageDAO(@NotNull final UserDAO userDAO,
                      @NotNull final TicketDAO ticketDAO,
                      @NotNull final ReservationDAO reservationDAO,
                      @NotNull final ExpoDAO expoDAO,
                      @NotNull final ArtworkDAO artworkDAO,
                      @NotNull final ClientOwnerPaymentDAO clientOwnerPaymentDAO,
                      @NotNull final OwnerArtistPaymentDAO ownerArtistPaymentDAO) {
        this.userDAO = userDAO;
        this.ticketDAO = ticketDAO;
        this.reservationDAO = reservationDAO;
        this.expoDAO = expoDAO;
        this.artworkDAO = artworkDAO;
        this.clientOwnerPaymentDAO = clientOwnerPaymentDAO;
        this.ownerArtistPaymentDAO = ownerArtistPaymentDAO;
    }

    @SneakyThrows
    private User getUser(@NotNull final String login) {
        final Optional<User> user = userDAO.findByLogin(login);
        return user.isEmpty() ? null : user.get();
    }

    @SneakyThrows
    public Client getClient(@NotNull final String login) {
        final User user = getUser(login);
        if (user == null){
            throw new UserNotFoundException(login);
        }
        else if (!user.isClient()){
            throw new IncorrectRoleException(login);
        }
        return (Client) user;
    }

    @SneakyThrows
    public Owner getOwner(@NotNull final String login) {
        final User user = getUser(login);
        if (user == null){
            throw new UserNotFoundException(login);
        }
        if (!user.isOwner()){
            throw new IncorrectRoleException(login);
        }
        return (Owner) user;
    }

    @SneakyThrows
    public Artist getArtist(@NotNull final String login) {
        final User user = getUser(login);
        if (user == null){
            throw new UserNotFoundException(login);
        }
        if (!user.isArtist()){
            throw new IncorrectRoleException(login);
        }
        return (Artist) user;
    }

    @SneakyThrows
    public long addUser(@NotNull final User user) {
        if (userDAO.findByLogin(user.getLogin()).isPresent())
            throw new UserAlreadyExistedException(user.getLogin());
        return userDAO.insert(user);
    }

    @SneakyThrows
    public void authenticateUser(@NotNull final String login,
                                 @NotNull final String password) {
        final User user = getUser(login);
        if (user == null){
            throw new UserNotFoundException(login);
        }
        user.signIn(password);
        updateUser(user);
    }

    public void authenticateUser(@NotNull final User user,
                                 @NotNull final String password) throws IncorrectPasswordException {
        if (!userDAO.authenticate(user, password)){
            throw new IncorrectPasswordException();
        }
    }

    @SneakyThrows
    public void signOut (@NotNull final String login) {
        final User user = getUser(login);
        if (user == null){
            throw new UserNotFoundException(login);
        }
        user.signOut();
        updateUser(user);
    }

    @SneakyThrows
    public Ticket getTicket(final long id) {
        Optional<Ticket> ticket = ticketDAO.findByID(id);
        return ticket.isEmpty() ? null : ticket.get();
    }

    @SneakyThrows
    public List<Ticket> getTicketByExpo(final long expo) {
        return ticketDAO.findByExpo(expo);
    }

    @SneakyThrows
    public Reservation getReservation(final long id) {
        Optional<Reservation> reservation = reservationDAO.findByID(id);
        return reservation.isEmpty() ? null : reservation.get();
    }

    @SneakyThrows
    public List<Reservation> getReservationsByStatus(@NotNull final String status) {
        return reservationDAO.findByStatus(status);
    }

    @SneakyThrows
    public Expo getExpo(final long id) {
        Optional<Expo> expo = expoDAO.findByID(id);
        return expo.isEmpty() ? null : expo.get();
    }

    @SneakyThrows
    public Artwork getArtwork(@NotNull final String name) {
        Optional<Artwork> artwork = artworkDAO.findByName(name);
        return artwork.isEmpty() ? null : artwork.get();
    }

    @SneakyThrows
    public ClientOwnerPayment getClientOwnerPayment(final long reservation) {
        Optional<ClientOwnerPayment> clientOwnerPayment = clientOwnerPaymentDAO.findByReservation(reservation);
        return clientOwnerPayment.isEmpty() ? null : clientOwnerPayment.get();
    }

    @SneakyThrows
    public OwnerArtistPayment getOwnerArtistPayment(final long expo) {
        Optional<OwnerArtistPayment> ownerArtistPayment = ownerArtistPaymentDAO.findByExpo(expo);
        return ownerArtistPayment.isEmpty() ? null : ownerArtistPayment.get();
    }

    public long addTicket(@NotNull final Ticket ticket) {
        return ticketDAO.insert(ticket);
    }

    public long addReservation(@NotNull final Reservation reservation) {
        return reservationDAO.insert(reservation);
    }

    public long addExpo(@NotNull final Expo expo) {
        return expoDAO.insert(expo);
    }

    public long addArtwork(@NotNull final Artwork artwork) {
        return artworkDAO.insert(artwork);
    }

    public long addClientOwnerPayment(@NotNull final ClientOwnerPayment clientOwnerPayment){
        return clientOwnerPaymentDAO.insert(clientOwnerPayment);
    }

    public long addOwnerArtistPayment(@NotNull final OwnerArtistPayment ownerArtistPayment){
        return ownerArtistPaymentDAO.insert(ownerArtistPayment);
    }

    public void updateUser(@NotNull final User user){
        userDAO.update(user);
    }

    public void updateReservation(@NotNull final Reservation reservation){
        reservationDAO.update(reservation);
    }

    public void updateExpo(@NotNull final Expo expo){
        expoDAO.update(expo);
    }

    public void updateArtwork(@NotNull final Artwork artwork){
        artworkDAO.update(artwork);
    }

}
