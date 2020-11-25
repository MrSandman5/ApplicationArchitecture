package com.example.galleryservice.model;

import com.example.galleryservice.GalleryServiceApplication;
import com.example.galleryservice.configuration.DataSourceConfig;
import com.example.galleryservice.data.StorageDAO;
import com.example.galleryservice.exceptions.ArtworkAlreadyExistedException;
import com.example.galleryservice.exceptions.ArtworkNotSimilarArtist;
import com.example.galleryservice.model.gallery.*;
import com.example.galleryservice.model.user.Artist;
import com.example.galleryservice.model.user.Client;
import com.example.galleryservice.model.user.Owner;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GalleryServiceApplication.class)
@ContextConfiguration(classes = DataSourceConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BusinessLogicTest {

    @Autowired
    private StorageDAO storageDAO;

    private Client client;
    private Owner owner;
    private Artist artist;
    private Expo expo;
    private Reservation reservation;

    @BeforeAll
    public void setUp() {
        client = storageDAO.getClient("client1");
        storageDAO.authenticateUser(client, "12345678");

        owner = storageDAO.getOwner("owner1");
        storageDAO.authenticateUser(owner, "31415926");

        artist = storageDAO.getArtist("artist1");
        storageDAO.authenticateUser(artist, "qwert123");
    }

    @AfterAll
    public void tearDown() {
        client = null;
        owner = null;
        artist = null;
    }

    @Order(1)
    @Test
    public void addArtworkTest() {
        final Artwork artwork1 = artist.addArtwork(new Artwork("#1", "First artwork (new beginning)", artist.getId()));
        assertTrue(artist.getArtworks().contains(artwork1.getName()), "Artwork successfully added to expo list");

        Exception exception = assertThrows(ArtworkAlreadyExistedException.class, () -> artist.addArtwork(artwork1));
        assertEquals("Artwork with name : " + artwork1.getName() + " already added to expo list!", exception.getMessage());

        final Artwork artwork2 = new Artwork("#2", "Second artwork (continuing)", artist.getId() + 1);
        exception = assertThrows(ArtworkNotSimilarArtist.class, () -> artist.addArtwork(artwork2));
        assertEquals("Artwork with name : " + artwork2.getName() + " doesn't belong to this artist!", exception.getMessage());

        final Artwork artwork3 = artist.addArtwork(new Artwork("#2", "Second artwork (continuing)", artist.getId()));
        assertTrue(artist.getArtworks().contains(artwork3.getName()), "Artwork successfully added to expo list");
    }

    @Order(2)
    @Test
    public void createExpoTest() {
        final Expo newExpo = new Expo("First", "First expo ever", artist.getId(),
                LocalDateTime.of(2020, Month.JUNE, 20, 3, 0),
                LocalDateTime.of(2020, Month.JUNE, 20, 3, 5),
                100, artist.getArtworks());
        expo = owner.createExpo(newExpo);
        assertTrue(owner.getExpos().contains(newExpo.getName()), "Expo successfully added to expo list");
    }

    @Order(3)
    @Test
    public void addTicketsTest() {
        final Ticket newTicket1 = client.addTicket(expo);
        assertTrue(client.getTickets().contains(newTicket1.getId()), "Ticket successfully added to client");

        final Ticket newTicket2 = client.addTicket(expo);
        assertTrue(client.getTickets().contains(newTicket2.getId()), "Ticket successfully added to client");
    }

    @Order(4)
    @Test
    public void createReservationTest() {
        reservation = client.createReservation();
        assertTrue(client.getReservations().contains(reservation.getId()), "Reservation successfully created");
    }

    @Order(5)
    @Test
    public void payForReservationTest() {
        client.payForReservation(reservation.getId(), owner);
        assertEquals(reservation.getCost(), storageDAO.getClientOwnerPayment(reservation.getId()).getPrice());
    }

    @Order(6)
    @Test
    public void acceptPaymentTest() {
        final double payment = owner.acceptPayment(reservation.getId());
        assertEquals(reservation.getCost(), payment);
    }

    @Order(7)
    @Test
    public void openExpoTest() {
        owner.startExpo(expo.getName());
        assertEquals(ExpoStatus.Opened, storageDAO.getExpo(expo.getName()).getStatus(), "Expo successfully opened");
    }

    @Order(8)
    @Test
    public void closeExpoTest() {
        owner.closeExpo(expo.getName());
        assertEquals(ExpoStatus.Closed, storageDAO.getExpo(expo.getName()).getStatus(), "Expo successfully closed");
    }

    @Order(9)
    @Test
    public void payForExpoTest() {
        owner.payForExpo(expo.getName());
        assertEquals(reservation.getCost(), storageDAO.getOwnerArtistPaymentByExpo(expo.getId()).getPrice());
    }

    @Order(10)
    @Test
    public void acceptRoyaltiesTest() {
        final double payment = artist.acceptRoyalties(expo.getName());
        assertEquals(reservation.getCost(), payment);
    }
}
