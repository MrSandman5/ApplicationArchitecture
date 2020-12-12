package com.safonov.galleryservice.ArtGalleryApplication.data;

import com.safonov.galleryservice.ArtGalleryApplication.data.gallery.*;
import com.safonov.galleryservice.ArtGalleryApplication.data.user.ArtistRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.user.ClientRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.user.OwnerRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.user.UserRepository;
import com.safonov.galleryservice.ArtGalleryApplication.exceptions.PaymentNotFoundException;
import com.safonov.galleryservice.ArtGalleryApplication.exceptions.UserAlreadyExistedException;
import com.safonov.galleryservice.ArtGalleryApplication.exceptions.UserNotFoundException;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.*;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Artist;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Client;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Owner;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.User;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
@Data
public class StorageRepository {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final OwnerRepository ownerRepository;
    private final ArtistRepository artistRepository;
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;
    private final ExpoRepository expoRepository;
    private final ArtworkRepository artworkRepository;
    private final PaymentRepository paymentRepository;
    private final ClientOwnerPaymentRepository clientOwnerPaymentRepository;
    private final OwnerArtistPaymentRepository ownerArtistPaymentRepository;

    @Autowired
    public StorageRepository(@NotNull final UserRepository userRepository,
                      @NotNull final ClientRepository clientRepository,
                      @NotNull final OwnerRepository ownerRepository,
                      @NotNull final ArtistRepository artistRepository,
                      @NotNull final TicketRepository ticketRepository,
                      @NotNull final ReservationRepository reservationRepository,
                      @NotNull final ExpoRepository expoRepository,
                      @NotNull final ArtworkRepository artworkRepository,
                      @NotNull final PaymentRepository paymentRepository,
                      @NotNull final ClientOwnerPaymentRepository clientOwnerPaymentRepository,
                      @NotNull final OwnerArtistPaymentRepository ownerArtistPaymentRepository) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.ownerRepository = ownerRepository;
        this.artistRepository = artistRepository;
        this.ticketRepository = ticketRepository;
        this.reservationRepository = reservationRepository;
        this.expoRepository = expoRepository;
        this.artworkRepository = artworkRepository;
        this.paymentRepository = paymentRepository;
        this.clientOwnerPaymentRepository = clientOwnerPaymentRepository;
        this.ownerArtistPaymentRepository = ownerArtistPaymentRepository;
    }

    @SneakyThrows
    public User getUser(@NotNull final String login) {
        return userRepository.findUserByLogin(login).orElse(null);
    }

    @SneakyThrows
    public User getUser(final long id) {
        return userRepository.findById(id).orElse(null);
    }

    @SneakyThrows
    public Client getClient(@NotNull final String login) {
        final User user = getUser(login);
        if (user == null){
            throw new UserNotFoundException(login);
        }
        final Client client = (Client) clientRepository.findById(user.getId()).orElse(null);
        if (client == null) {
            return client;
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
        final Client client = (Client) clientRepository.findById(user.getId()).orElse(null);
        if (client == null) {
            return client;
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
        final Owner owner = (Owner) ownerRepository.findById(user.getId()).orElse(null);
        if (owner == null) {
            return owner;
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
        final Owner owner = (Owner) ownerRepository.findById(user.getId()).orElse(null);
        if (owner == null) {
            return owner;
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
        final Artist artist = (Artist) artistRepository.findByID(user.getId()).orElse(null);
        if (artist == null) {
            return artist;
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
        final Artist artist = (Artist) artistRepository.findByID(user.getId()).orElse(null);
        if (artist == null) {
            return artist;
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
        if (userRepository.findUserByLogin(login).isPresent())
            throw new UserAlreadyExistedException(login);
        final User user = new User(login, password, name, email);
        return userRepository.insert(user);
    }

    @SneakyThrows
    public long addUser(@NotNull final User user) {
        if (userRepository.findUserByLogin(user.getLogin()).isPresent())
            throw new UserAlreadyExistedException(user.getLogin());
        return userRepository.insert(user);
    }

    @SneakyThrows
    public void addClient(@NotNull final Client client) {
        final long clientId = addUser(client);
        client.setId(clientId);
        clientRepository.insert(client);
    }

    @SneakyThrows
    public void addOwner(@NotNull final Owner owner) {
        final long ownerId = addUser(owner);
        owner.setId(ownerId);
        ownerRepository.insert(owner);
    }

    @SneakyThrows
    public void addArtist(@NotNull final Artist artist) {
        final long artistId = addUser(artist);
        artist.setId(artistId);
        artistRepository.insert(artist);
    }

    /*@SneakyThrows
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
    }*/

    @SneakyThrows
    public Ticket getTicket(final long id) {
        return ticketRepository.findByID(id).orElse(null);
    }

    @SneakyThrows
    public List<Ticket> getTicketsByExpo(final Expo expo) {
        return ticketRepository.findTicketsByExpo(expo);
    }

    @SneakyThrows
    public Reservation getReservation(final long id) {
        return reservationRepository.findByID(id).orElse(null);
    }

    @SneakyThrows
    public List<Reservation> getReservationsByStatus(@NotNull final ReservationStatus status) {
        return reservationRepository.findReservationsByStatus(status);
    }

    @SneakyThrows
    public Expo getExpo(@NotNull final String name) {
        return expoRepository.findExpoByName(name).orElse(null);
    }

    @SneakyThrows
    public Expo getExpo(final long id) {
        return expoRepository.findByID(id).orElse(null);
    }

    @SneakyThrows
    public Artwork getArtwork(@NotNull final String name) {
        return artworkRepository.findArtworkByName(name).orElse(null);
    }

    @SneakyThrows
    public Artwork getArtwork(final long id) {
        return artworkRepository.findByID(id).orElse(null);
    }

    @SneakyThrows
    public Payment getPayment(final long id) {
        return paymentRepository.findByID(id).orElse(null);
    }

    @SneakyThrows
    public ClientOwnerPayment getClientOwnerPaymentByReservation(final Reservation reservation) {
        final ClientOwnerPayment clientOwnerPayment =
                clientOwnerPaymentRepository.findPaymentByReservation(reservation).orElse(null);
        if (clientOwnerPayment == null) {
            return clientOwnerPayment;
        }
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
        final ClientOwnerPayment clientOwnerPayment =
                (ClientOwnerPayment) clientOwnerPaymentRepository.findByID(id).orElse(null);
        if (clientOwnerPayment == null){
            return clientOwnerPayment;
        }
        clientOwnerPayment.setDateTime(payment.getDateTime());
        clientOwnerPayment.setPrice(payment.getPrice());
        return clientOwnerPayment;
    }

    @SneakyThrows
    public OwnerArtistPayment getOwnerArtistPaymentByExpo(final Expo expo) {
        final OwnerArtistPayment ownerArtistPayment =
                ownerArtistPaymentRepository.findPaymentByExpo(expo).orElse(null);
        if (ownerArtistPayment == null) {
            return ownerArtistPayment;
        }
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
        final OwnerArtistPayment ownerArtistPayment =
                (OwnerArtistPayment) ownerArtistPaymentRepository.findByID(id).orElse(null);
        if (ownerArtistPayment == null){
            return ownerArtistPayment;
        }
        ownerArtistPayment.setDateTime(payment.getDateTime());
        ownerArtistPayment.setPrice(payment.getPrice());
        return ownerArtistPayment;
    }

    public long addTicket(@NotNull final Ticket ticket) {
        return ticketRepository.insert(ticket);
    }

    public long addReservation(@NotNull final Reservation reservation) {
        return reservationRepository.insert(reservation);
    }

    public long addExpo(@NotNull final Expo expo) {
        return expoRepository.insert(expo);
    }

    public long addArtwork(@NotNull final Artwork artwork) {
        return artworkRepository.insert(artwork);
    }

    private long addPayment(@NotNull final Payment payment) {
        return paymentRepository.insert(payment);
    }

    public long addClientOwnerPayment(@NotNull final ClientOwnerPayment clientOwnerPayment){
        final long paymentId = addPayment(clientOwnerPayment);
        clientOwnerPayment.setId(paymentId);
        clientOwnerPaymentRepository.insert(clientOwnerPayment);
        return paymentId;
    }

    public long addOwnerArtistPayment(@NotNull final OwnerArtistPayment ownerArtistPayment){
        final long paymentId = addPayment(ownerArtistPayment);
        ownerArtistPayment.setId(paymentId);
        ownerArtistPaymentRepository.insert(ownerArtistPayment);
        return paymentId;
    }

    public void updateUser(@NotNull final User user){
        userRepository.update(user);
    }

    public void updateClient(@NotNull final Client client){
        updateUser(client);
        clientRepository.update(client);
    }

    public void updateOwner(@NotNull final Owner owner){
        updateUser(owner);
        ownerRepository.update(owner);
    }

    public void updateArtist(@NotNull final Artist artist){
        updateUser(artist);
        artistRepository.update(artist);
    }

    public void updateReservation(@NotNull final Reservation reservation){
        reservationRepository.update(reservation);
    }

    public void updateExpo(@NotNull final Expo expo){
        expoRepository.update(expo);
    }
}
