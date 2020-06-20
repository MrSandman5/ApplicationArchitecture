package com.example.galleryservice.data;

import com.example.galleryservice.data.gallery.*;
import com.example.galleryservice.data.user.ArtistDAO;
import com.example.galleryservice.data.user.ClientDAO;
import com.example.galleryservice.data.user.OwnerDAO;
import com.example.galleryservice.data.user.UserDAO;
import com.example.galleryservice.exceptions.*;
import com.example.galleryservice.model.gallery.*;
import com.example.galleryservice.model.user.*;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
@Data
public class StorageDAO {

    private final UserDAO userDAO;
    private final ClientDAO clientDAO;
    private final OwnerDAO ownerDAO;
    private final ArtistDAO artistDAO;
    private final TicketDAO ticketDAO;
    private final ReservationDAO reservationDAO;
    private final ExpoDAO expoDAO;
    private final ArtworkDAO artworkDAO;
    private final PaymentDAO paymentDAO;
    private final ClientOwnerPaymentDAO clientOwnerPaymentDAO;
    private final OwnerArtistPaymentDAO ownerArtistPaymentDAO;

    @Autowired
    public StorageDAO(@NotNull final UserDAO userDAO,
                      @NotNull final ClientDAO clientDAO,
                      @NotNull final OwnerDAO ownerDAO,
                      @NotNull final ArtistDAO artistDAO,
                      @NotNull final TicketDAO ticketDAO,
                      @NotNull final ReservationDAO reservationDAO,
                      @NotNull final ExpoDAO expoDAO,
                      @NotNull final ArtworkDAO artworkDAO,
                      @NotNull final PaymentDAO paymentDAO,
                      @NotNull final ClientOwnerPaymentDAO clientOwnerPaymentDAO,
                      @NotNull final OwnerArtistPaymentDAO ownerArtistPaymentDAO) {
        this.userDAO = userDAO;
        this.clientDAO = clientDAO;
        this.ownerDAO = ownerDAO;
        this.artistDAO = artistDAO;
        this.ticketDAO = ticketDAO;
        this.reservationDAO = reservationDAO;
        this.expoDAO = expoDAO;
        this.artworkDAO = artworkDAO;
        this.paymentDAO = paymentDAO;
        this.clientOwnerPaymentDAO = clientOwnerPaymentDAO;
        this.ownerArtistPaymentDAO = ownerArtistPaymentDAO;
    }

    @SneakyThrows
    public User getUser(@NotNull final String login) {
        return userDAO.findByLogin(login);
    }

    @SneakyThrows
    public User getUser(final long id) {
        return userDAO.findByID(id);
    }

    @SneakyThrows
    public Client getClient(@NotNull final String login) {
        final User user = getUser(login);
        if (user == null){
            throw new UserNotFoundException(login);
        }
        final Client client = clientDAO.findByID(user.getId());
        if (client == null) {
            return null;
        }
        client.setLogin(user.getLogin());
        client.setPassword(user.getPassword());
        client.setName(user.getName());
        client.setEmail(user.getEmail());
        client.setAuthentication(user.getAuthentication());
        client.setLogin(user.getLogin());
        return client;
    }

    @SneakyThrows
    public Client getClient(final long id) {
        final User user = getUser(id);
        if (user == null){
            throw new UserNotFoundException(id);
        }
        final Client client = clientDAO.findByID(user.getId());
        if (client == null) {
            return null;
        }
        client.setLogin(user.getLogin());
        client.setPassword(user.getPassword());
        client.setName(user.getName());
        client.setEmail(user.getEmail());
        client.setAuthentication(user.getAuthentication());
        client.setLogin(user.getLogin());
        return client;
    }

    @SneakyThrows
    public Owner getOwner(@NotNull final String login) {
        final User user = getUser(login);
        if (user == null){
            throw new UserNotFoundException(login);
        }
        final Owner owner = ownerDAO.findByID(user.getId());
        if (owner == null) {
            return null;
        }
        owner.setLogin(user.getLogin());
        owner.setPassword(user.getPassword());
        owner.setName(user.getName());
        owner.setEmail(user.getEmail());
        owner.setAuthentication(user.getAuthentication());
        owner.setLogin(user.getLogin());
        return owner;
    }

    @SneakyThrows
    public Owner getOwner(final long id) {
        final User user = getUser(id);
        if (user == null){
            throw new UserNotFoundException(id);
        }
        final Owner owner = ownerDAO.findByID(user.getId());
        if (owner == null) {
            return null;
        }
        owner.setLogin(user.getLogin());
        owner.setPassword(user.getPassword());
        owner.setName(user.getName());
        owner.setEmail(user.getEmail());
        owner.setAuthentication(user.getAuthentication());
        owner.setLogin(user.getLogin());
        return owner;
    }

    @SneakyThrows
    public Artist getArtist(@NotNull final String login) {
        final User user = getUser(login);
        if (user == null){
            throw new UserNotFoundException(login);
        }
        final Artist artist = artistDAO.findByID(user.getId());
        if (artist == null) {
            return null;
        }
        artist.setLogin(user.getLogin());
        artist.setPassword(user.getPassword());
        artist.setName(user.getName());
        artist.setEmail(user.getEmail());
        artist.setAuthentication(user.getAuthentication());
        artist.setLogin(user.getLogin());
        return artist;
    }

    @SneakyThrows
    public Artist getArtist(final long id) {
        final User user = getUser(id);
        if (user == null){
            throw new UserNotFoundException(id);
        }
        final Artist artist = artistDAO.findByID(user.getId());
        if (artist == null) {
            return null;
        }
        artist.setLogin(user.getLogin());
        artist.setPassword(user.getPassword());
        artist.setName(user.getName());
        artist.setEmail(user.getEmail());
        artist.setAuthentication(user.getAuthentication());
        artist.setLogin(user.getLogin());
        return artist;
    }

    @SneakyThrows
    public long addUser(@NotNull final String login,
                        @NotNull final String password,
                        @NotNull final String name,
                        @NotNull final String email) {
        if (userDAO.findByLogin(login) != null)
            throw new UserAlreadyExistedException(login);
        final User user = new User(login, password, name, email);
        return userDAO.insert(user);
    }

    @SneakyThrows
    public long addUser(@NotNull final User user) {
        if (userDAO.findByLogin(user.getLogin()) != null)
            throw new UserAlreadyExistedException(user.getLogin());
        return userDAO.insert(user);
    }

    @SneakyThrows
    public void addClient(@NotNull final Client client) {
        final long clientId = addUser(client);
        client.setId(clientId);
        clientDAO.insert(client);
    }

    @SneakyThrows
    public void addOwner(@NotNull final Owner owner) {
        final long ownerId = addUser(owner);
        owner.setId(ownerId);
        ownerDAO.insert(owner);
    }

    @SneakyThrows
    public void addArtist(@NotNull final Artist artist) {
        final long artistId = addUser(artist);
        artist.setId(artistId);
        artistDAO.insert(artist);
    }

    @SneakyThrows
    public void authenticate(@NotNull final String login,
                                 @NotNull final String password) {
        final User user = getUser(login);
        if (user == null){
            throw new UserNotFoundException(login);
        }
        user.signIn(password);
        updateUser(user);
    }

    @SneakyThrows
    public void authenticateUser(@NotNull final User user,
                                 @NotNull final String password) {
        user.signIn(password);
        updateUser(user);
    }

    @SneakyThrows
    public void signOut(@NotNull final String login) {
        final User user = getUser(login);
        if (user == null){
            throw new UserNotFoundException(login);
        }
        user.signOut();
        updateUser(user);
    }

    @SneakyThrows
    public Ticket getTicket(final long id) {
        return ticketDAO.findByID(id);
    }

    @SneakyThrows
    public List<Ticket> getTicketByExpo(final Long expo) {
        return ticketDAO.findByExpo(expo);
    }

    @SneakyThrows
    public Reservation getReservation(final long id) {
        return reservationDAO.findByID(id);
    }

    @SneakyThrows
    public List<Reservation> getReservationsByStatus(@NotNull final String status) {
        return reservationDAO.findByStatus(status);
    }

    @SneakyThrows
    public Expo getExpo(@NotNull final String name) {
        return expoDAO.findByName(name);
    }

    @SneakyThrows
    public Expo getExpo(final long id) {
        return expoDAO.findByID(id);
    }

    @SneakyThrows
    public Artwork getArtwork(@NotNull final String name) {
        return artworkDAO.findByName(name);
    }

    @SneakyThrows
    public Artwork getArtwork(final long id) {
        return artworkDAO.findByID(id);
    }

    @SneakyThrows
    public Payment getPayment(final long id) {
        return paymentDAO.findByID(id);
    }

    @SneakyThrows
    public ClientOwnerPayment getClientOwnerPaymentByReservation(final long reservation) {
        final ClientOwnerPayment clientOwnerPayment = clientOwnerPaymentDAO.findByReservation(reservation);
        final Payment payment = getPayment(clientOwnerPayment.getId());
        clientOwnerPayment.setDateTime(payment.getDateTime());
        clientOwnerPayment.setPrice(payment.getPrice());
        return clientOwnerPayment;
    }

    @SneakyThrows
    public ClientOwnerPayment getClientOwnerPayment(final long id) {
        final Payment payment = getPayment(id);
        if (payment == null){
            throw new PaymentNotFoundException(id);
        }
        final ClientOwnerPayment clientOwnerPayment = clientOwnerPaymentDAO.findByID(id);
        if (clientOwnerPayment == null){
            return null;
        }
        clientOwnerPayment.setDateTime(payment.getDateTime());
        clientOwnerPayment.setPrice(payment.getPrice());
        return clientOwnerPayment;
    }

    @SneakyThrows
    public OwnerArtistPayment getOwnerArtistPaymentByExpo(final long expo) {
        final OwnerArtistPayment ownerArtistPayment = ownerArtistPaymentDAO.findByExpo(expo);
        final Payment payment = getPayment(ownerArtistPayment.getId());
        ownerArtistPayment.setDateTime(payment.getDateTime());
        ownerArtistPayment.setPrice(payment.getPrice());
        return ownerArtistPayment;
    }

    @SneakyThrows
    public OwnerArtistPayment getOwnerArtistPayment(final long id) {
        final Payment payment = getPayment(id);
        if (payment == null){
            throw new PaymentNotFoundException(id);
        }
        final OwnerArtistPayment ownerArtistPayment = ownerArtistPaymentDAO.findByID(id);
        if (ownerArtistPayment == null){
            return null;
        }
        ownerArtistPayment.setDateTime(payment.getDateTime());
        ownerArtistPayment.setPrice(payment.getPrice());
        return ownerArtistPayment;
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

    private long addPayment(@NotNull final Payment payment) {
        return paymentDAO.insert(payment);
    }

    public long addClientOwnerPayment(@NotNull final ClientOwnerPayment clientOwnerPayment){
        final long paymentId = addPayment(clientOwnerPayment);
        clientOwnerPayment.setId(paymentId);
        clientOwnerPaymentDAO.insert(clientOwnerPayment);
        return paymentId;
    }

    public long addOwnerArtistPayment(@NotNull final OwnerArtistPayment ownerArtistPayment){
        final long paymentId = addPayment(ownerArtistPayment);
        ownerArtistPayment.setId(paymentId);
        ownerArtistPaymentDAO.insert(ownerArtistPayment);
        return paymentId;
    }

    public void updateUser(@NotNull final User user){
        userDAO.update(user);
    }

    public void updateClient(@NotNull final Client client){
        updateUser(client);
        clientDAO.update(client);
    }

    public void updateOwner(@NotNull final Owner owner){
        updateUser(owner);
        userDAO.update(owner);
    }

    public void updateArtist(@NotNull final Artist artist){
        updateUser(artist);
        userDAO.update(artist);
    }

    public void updateReservation(@NotNull final Reservation reservation){
        reservationDAO.update(reservation);
    }

    public void updateExpo(@NotNull final Expo expo){
        expoDAO.update(expo);
    }

}
