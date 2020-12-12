package com.safonov.galleryservice.ArtGalleryApplication.data;

import com.safonov.galleryservice.ArtGalleryApplication.ArtGalleryApplication;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Artist;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Client;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Owner;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArtGalleryApplication.class)
public class StorageRepositoryTest {

    @Autowired
    private StorageRepository storageRepository;

    @Test
    public void addUserTest() {
        final Client client = new Client("client1", "qwertyuio", "Vasya Pupkin", "vasya_pupkin@mail.ru");
        storageRepository.addClient(client);
        assertEquals(client.getLogin(), storageRepository.getUser("client1").getLogin(), "Client successfully added to DB");

        final Owner owner = new Owner("owner", "123456", "Vladimir Putin", "vladimir_putin@mail.ru");
        storageRepository.addOwner(owner);
        assertEquals(owner.getLogin(), storageRepository.getUser("owner").getLogin(), "Owner successfully added to DB");

        final Artist artist = new Artist("artist1", "31415926", "Lampas Pokras", "lampas_pokras@mail.ru");
        storageRepository.addArtist(artist);
        assertEquals(artist.getLogin(), storageRepository.getUser("artist1").getLogin(), "Artist successfully added to DB");
    }

    /*@Test
    public void authenticateUserTest() {
        final Client client = new Client("client1", "qwertyuio", "Vasya Pupkin", "vasya_pupkin@mail.ru");
        storageRepository.addClient(client);
        assertEquals(client.getLogin(), storageRepository.getUser("client1").getLogin(), "Client successfully added to DB");

        Exception exception = assertThrows(UndeclaredThrowableException.class, () -> storageRepository.authenticateUser(client, "password"));
        //assertEquals("Incorrect login or password", exception.getMessage());

        storageRepository.authenticateUser(client, "qwertyuio");
        assertEquals(true, client.getAuthentication(), "Authenticated with correct password");
    }*/
}
