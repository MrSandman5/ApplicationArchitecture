package com.example.galleryservice.data;

import com.example.galleryservice.data.project.*;
import com.example.galleryservice.data.user.UserDAO;
import com.example.galleryservice.exceptions.*;
import com.example.galleryservice.model.project.*;
import com.example.galleryservice.model.user.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public class StorageDAO {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private TicketDAO ticketDAO;
    @Autowired
    private ReservationDAO reservationDAO;
    @Autowired
    private ExpoDAO expoDAO;
    @Autowired
    private ArtworkDAO artworkDAO;
    @Autowired
    private ClientOwnerPaymentDAO clientOwnerPaymentDAO;
    @Autowired
    private OwnerArtistPaymentDAO ownerArtistPaymentDAO;

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
    public void addUser(@NotNull final User user) {
        if (userDAO.findByLogin(user.getLogin()).isPresent())
            throw new UserAlreadyExistedException(user.getLogin());
        userDAO.insert(user);
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

    public void addTicket(@NotNull final Ticket ticket) {
        ticketDAO.insert(ticket);
    }

    public void addReservation(@NotNull final Reservation reservation) {
        reservationDAO.insert(reservation);
    }

    public void addExpo(@NotNull final Expo expo) {
        expoDAO.insert(expo);
    }

    public void addArtwork(@NotNull final Artwork artwork) {
        artworkDAO.insert(artwork);
    }

    public void addClientOwnerPayment(@NotNull final ClientOwnerPayment clientOwnerPayment){
        clientOwnerPaymentDAO.insert(clientOwnerPayment);
    }

    public void addOwnerArtistPayment(@NotNull final OwnerArtistPayment ownerArtistPayment){
        ownerArtistPaymentDAO.insert(ownerArtistPayment);
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
