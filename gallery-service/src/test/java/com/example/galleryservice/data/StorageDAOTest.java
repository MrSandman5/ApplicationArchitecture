package com.example.galleryservice.data;

import com.example.galleryservice.GalleryServiceApplication;
import com.example.galleryservice.configuration.DataSourceConfig;
import com.example.galleryservice.model.user.Artist;
import com.example.galleryservice.model.user.Client;
import com.example.galleryservice.model.user.Owner;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GalleryServiceApplication.class)
@ContextConfiguration(classes = DataSourceConfig.class)
public class StorageDAOTest {

    @Autowired
    private StorageDAO storageDAO;

    @Test
    public void addUserTest() {
        final Client client = new Client("client1", "qwertyuio", "Vasya Pupkin", "vasya_pupkin@mail.ru");
        storageDAO.addClient(client);
        assertEquals(client.getLogin(), storageDAO.getUser("client1").getLogin(), "Client successfully added to DB");

        final Owner owner = new Owner("owner", "123456", "Vladimir Putin", "vladimir_putin@mail.ru");
        storageDAO.addOwner(owner);
        assertEquals(owner.getLogin(), storageDAO.getUser("owner").getLogin(), "Owner successfully added to DB");

        final Artist artist = new Artist("artist1", "31415926", "Lampas Pokras", "lampas_pokras@mail.ru");
        storageDAO.addArtist(artist);
        assertEquals(artist.getLogin(), storageDAO.getUser("artist1").getLogin(), "Artist successfully added to DB");
    }

    @Test
    public void authenticateUserTest() {
        final Client client = new Client("client1", "qwertyuio", "Vasya Pupkin", "vasya_pupkin@mail.ru");
        storageDAO.addClient(client);
        assertEquals(client.getLogin(), storageDAO.getUser("client1").getLogin(), "Client successfully added to DB");

        Exception exception = assertThrows(UndeclaredThrowableException.class, () -> storageDAO.authenticateUser(client, "password"));
        //assertEquals("Incorrect login or password", exception.getMessage());

        storageDAO.authenticateUser(client, "qwertyuio");
        assertEquals(true, client.getAuthentication(), "Authenticated with correct password");
    }

}
