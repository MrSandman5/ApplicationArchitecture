package com.safonov.galleryservice.ArtGalleryApplication.service;

import com.github.javafaker.Faker;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.ArtistRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.ClientRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.CredentialsRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.OwnerRepository;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Artist;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Client;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Credentials;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Owner;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DataGenerator {
    /*private static final Logger log = LoggerFactory.getLogger(DataGenerator.class);

    Faker faker = new Faker();

    private int clientsCount = 0;
    private int ownersCount = 0;
    private int artistsCount = 0;
    private int clientsCountCatch = 0;
    private int ownersCountCatch = 0;
    private int artistsCountCatch = 0;

    private final static int workload = 12;
    private final ClientRepository clientRepository;
    private final OwnerRepository ownerRepository;
    private final ArtistRepository artistRepository;
    private final CredentialsRepository credentialsRepository;
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;
    private final ExpoRepository expoRepository;
    private final ArtworkRepository artworkRepository;
    private final Date startDate = new SimpleDateFormat("dd.MM.yyyy").parse("01.01.2020");

    @Autowired
    DataGenerator(@NotNull final ClientRepository clientRepository,
                  @NotNull final OwnerRepository ownerRepository,
                  @NotNull final ArtistRepository artistRepository,
                  @NotNull final CredentialsRepository credentialsRepository
                  @NotNull final TicketRepository ticketRepository,
                  @NotNull final ReservationRepository reservationRepository,
                  @NotNull final ExpoRepository expoRepository,
                  @NotNull final ArtworkRepository artworkRepository) throws ParseException {
        this.clientRepository = clientRepository;
        this.ownerRepository = ownerRepository;
        this.artistRepository = artistRepository;
        this.credentialsRepository = credentialsRepository;
        this.ticketRepository = ticketRepository;
        this.reservationRepository = reservationRepository;
        this.expoRepository = expoRepository;
        this.artworkRepository = artworkRepository;
    }

    public String generateFakeDataForClient(final int count) {
        for (int i = 0; i < count; i++) {
            try {
                final Client client = new Client(faker.name().firstName(), faker.name().lastName());
                final Credentials credentials = new Credentials(faker.book().title().replaceAll(" ", "") + "@mail.ru",
                        hashPassword(faker.book().publisher()));
                client.setCredentials(credentialsRepository.save(credentials));
                client.setDeleted(faker.bool().bool());
                clientRepository.save(client);
                clientsCount++;
            } catch (Exception ex) {
                log.error("Fake data generation for client failed", ex);
                clientsCountCatch++;
            }
        }
        return "Generate " + clientsCount + " fake clients\n" +
                "Catch  " + clientsCountCatch + " clients\n";
    }

    public String generateFakeDataForOwner(final int count) {
        for (int i = 0; i < count; i++) {
            try {
                final Owner owner = new Owner(faker.name().firstName(), faker.name().lastName());
                final Credentials credentials = new Credentials(faker.harryPotter().spell().replaceAll(" ", "") + "@mail.ru",
                        hashPassword(faker.harryPotter().location()));
                owner.setCredentials(credentialsRepository.save(credentials));
                owner.setDeleted(faker.bool().bool());
                ownerRepository.save(owner);
                ownersCount++;
            } catch (Exception ex) {
                log.error("Fake data generation for owner failed", ex);
                ownersCountCatch++;
            }
        }
        return "Generate " + ownersCount + " fake owners\n" +
                "Catch " + ownersCountCatch + " owners\n";
    }

    public String generateFakeDataForArtist(final int count) {
        for (int i = 0; i < count; i++) {
            try {
                final Artist artist = new Artist(faker.name().firstName(), faker.name().lastName());
                final Credentials credentials = new Credentials(faker.zelda().character().replaceAll(" ", "") + "@mail.ru",
                        hashPassword(faker.harryPotter().location()));
                artist.setCredentials(credentialsRepository.save(credentials));
                artist.setDeleted(faker.bool().bool());
                artistRepository.save(artist);
                artistsCount++;
            } catch (Exception ex) {
                log.error("Fake data generation for artist failed", ex);
                artistsCountCatch++;
            }
        }
        return "Generate " + ownersCount + " fake artists\n" +
                "Catch " + artistsCountCatch + " artists\n";
    }

    public static String hashPassword(@NotNull final String password_plaintext) {
        final String salt = BCrypt.gensalt(workload);
        return (BCrypt.hashpw(password_plaintext, salt));
    }*/
}
