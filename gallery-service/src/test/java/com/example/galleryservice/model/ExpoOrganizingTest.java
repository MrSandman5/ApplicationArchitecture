package com.example.galleryservice.model;

import com.example.galleryservice.GalleryServiceApplication;
import com.example.galleryservice.configuration.DataSourceConfig;
import com.example.galleryservice.data.StorageDAO;
import com.example.galleryservice.exceptions.ArtworkAlreadyExistedException;
import com.example.galleryservice.exceptions.ArtworkNotSimilarArtist;
import com.example.galleryservice.model.gallery.Artwork;
import com.example.galleryservice.model.gallery.Expo;
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

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GalleryServiceApplication.class)
@ContextConfiguration(classes = DataSourceConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExpoOrganizingTest {

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

    @Test
    public void addArtworkTest() {
        final Artwork artwork1 = artist.addArtwork(new Artwork("#1", "First artwork (new beginning)", artist.getId()));
        assertTrue("Artwork successfully added to expo list", artist.getArtworks().contains(artwork1));

        Exception exception = assertThrows(ArtworkAlreadyExistedException.class, () -> artist.addArtwork(artwork1));
        assertEquals("Artwork with name : " + artwork1.getName() + " already added to expo list!", exception.getMessage());

        final Artwork artwork2 = new Artwork("#2", "Second artwork (continuing)", artist.getId() + 1);
        exception = assertThrows(ArtworkNotSimilarArtist.class, () -> artist.addArtwork(artwork2));
        assertEquals("Artwork with name : " + artwork2.getName() + " doesn't belong to this artist!", exception.getMessage());

        final Artwork artwork3 = artist.addArtwork(new Artwork("#2", "Second artwork (continuing)", artist.getId()));
        assertTrue("Artwork successfully added to expo list", artist.getArtworks().contains(artwork3));
    }

    @Test
    public void createExpoTest() {
        final Expo newExpo = new Expo("First", "First expo ever", artist.getId(),
                LocalDateTime.of(2020, Month.JUNE, 14, 0, 0),
                LocalDateTime.of(2020, Month.JUNE, 17, 0, 0),
                100, artist.getArtworks());
        owner.createExpo(newExpo);
        assertTrue("Expo successfully added to expo list", owner.getExpos().contains(newExpo));
    }

    @AfterAll
    public void tearDown() {
        client = null;
        owner = null;
        artist = null;
    }
}
