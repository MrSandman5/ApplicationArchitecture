package com.example.galleryservice.data;

import com.example.galleryservice.GalleryServiceApplication;
import com.example.galleryservice.configuration.DataSourceConfig;
import com.example.galleryservice.exceptions.ArtworkAlreadyExistedException;
import com.example.galleryservice.exceptions.IncorrectPasswordException;
import com.example.galleryservice.model.user.Artist;
import com.example.galleryservice.model.user.Client;
import com.example.galleryservice.model.user.Owner;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GalleryServiceApplication.class)
@ContextConfiguration(classes = DataSourceConfig.class)
public class StorageDAOTest {

    @Autowired
    private StorageDAO storageDAO;

    @Test
    public void addUserTest() {
        final Client client = new Client("client1", "qwertyuio", "Vasya Pupkin", "vasya_pupkin@mail.ru");
        long clientId = storageDAO.addUser(client);
        System.out.println("Generated id for client: " + clientId);
        assertEquals("Client successfully added to DB", client.getLogin(), storageDAO.getUserDAO().findByID(clientId).getLogin());

        final Owner owner = new Owner("owner", "123456", "Vladimir Putin", "vladimir_putin@mail.ru");
        long ownerId = storageDAO.addUser(owner);
        System.out.println("Generated id for owner: " + ownerId);
        assertEquals("Owner successfully added to DB", owner.getLogin(), storageDAO.getUserDAO().findByID(ownerId).getLogin());

        final Artist artist = new Artist("artist1", "31415926", "Lampas Pokras", "lampas_pokras@mail.ru");
        long artistId = storageDAO.addUser(artist);
        System.out.println("Generated id for artist: " + artistId);
        assertEquals("Artist successfully added to DB", artist.getLogin(), storageDAO.getUserDAO().findByID(artistId).getLogin());
    }

    @Test
    public void authenticateUserTest() {
        final Client client = new Client("client1", "qwertyuio", "Vasya Pupkin", "vasya_pupkin@mail.ru");
        long clientId = storageDAO.addUser(client);
        System.out.println("Generated id for client: " + clientId);
        assertEquals("Client successfully added to DB", client.getLogin(), storageDAO.getUserDAO().findByID(clientId).getLogin());


        Exception exception = assertThrows(IncorrectPasswordException.class, () -> storageDAO.authenticateUser(client, "pass2"));
        assertEquals("Incorrect login or password", exception.getMessage());

        storageDAO.authenticateUser(client, "qwertyuio");
        assertEquals("Authenticated with correct password", true, client.getAuthentication());
    }

}
