package com.example.galleryservice.model;

import com.example.galleryservice.GalleryServiceApplication;
import com.example.galleryservice.configuration.DataSourceConfig;
import com.example.galleryservice.data.StorageDAO;
import com.example.galleryservice.model.gallery.Expo;
import com.example.galleryservice.model.gallery.Ticket;
import com.example.galleryservice.model.user.Artist;
import com.example.galleryservice.model.user.Client;
import com.example.galleryservice.model.user.Owner;
import junit.framework.TestCase;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GalleryServiceApplication.class)
@ContextConfiguration(classes = DataSourceConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TicketPurchasingTest extends TestCase {

    @Autowired
    private StorageDAO storageDAO;

    private Client client;
    private Owner owner;
    private Artist artist;

    @BeforeAll
    public void setUp() {
        client = storageDAO.getClient("client1");
        storageDAO.authenticateUser(client, "12345678");

        owner = storageDAO.getOwner("owner1");
        storageDAO.authenticateUser(owner, "31415926");

        artist = storageDAO.getArtist("artist1");
        storageDAO.authenticateUser(artist, "qwert");
    }

    public void addTicketsTest() {
        final Expo targetExpo = storageDAO.getExpo("First");
        final Ticket ticket1 = new Ticket(client.getId(), targetExpo.getId(), targetExpo.getTicketPrice());
        client.addTicket(ticket1);
        assertTrue("Ticket successfully added to client", client.getTickets().contains(ticket1));

        final Ticket ticket2 = new Ticket(client.getId(), targetExpo.getId(), targetExpo.getTicketPrice());
        client.addTicket(ticket2);
        assertTrue("Ticket successfully added to client", client.getTickets().contains(ticket2));
    }

    @AfterAll
    public void tearDown() {
        client = null;
        owner = null;
        artist = null;
    }

}
