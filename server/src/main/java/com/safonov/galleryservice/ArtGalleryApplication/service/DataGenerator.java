package com.safonov.galleryservice.ArtGalleryApplication.service;

import com.github.javafaker.Faker;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.*;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Artist;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Client;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Credentials;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Owner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class DataGenerator {
    private static final Logger log = LoggerFactory.getLogger(DataGenerator.class);

    Faker faker = new Faker();

    private int clientsCount = 0;
    private int ownersCount = 0;
    private int artistsCount = 0;
    private int clientsCountCatch = 0;
    private int ownersCountCatch = 0;
    private int artistsCountCatch = 0;

    private final ClientRepository clientRepository;
    private final OwnerRepository ownerRepository;
    private final ArtistRepository artistRepository;
    private final CredentialsRepository credentialsRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public DataGenerator(@NotNull final ClientRepository clientRepository,
                         @NotNull final OwnerRepository ownerRepository,
                         @NotNull final ArtistRepository artistRepository,
                         @NotNull final CredentialsRepository credentialsRepository,
                         @NotNull final RoleRepository roleRepository,
                         @NotNull final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.clientRepository = clientRepository;
        this.ownerRepository = ownerRepository;
        this.artistRepository = artistRepository;
        this.credentialsRepository = credentialsRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public String generateFakeDataForClient(final int count) {
        for (int i = 0; i < count; i++) {
            try {
                final Client client = new Client(faker.name().firstName(), faker.name().lastName());
                final Credentials credentials = new Credentials(faker.book().title().replaceAll(" ", "") + "@mail.ru",
                        bCryptPasswordEncoder.encode((faker.book().publisher())), roleRepository.findRoleByName("ROLE_CLIENT").orElse(null));
                client.setCredentials(credentialsRepository.save(credentials));
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
                        bCryptPasswordEncoder.encode((faker.book().publisher())), roleRepository.findRoleByName("ROLE_OWNER").orElse(null));
                owner.setCredentials(credentialsRepository.save(credentials));
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
                        bCryptPasswordEncoder.encode((faker.book().publisher())), roleRepository.findRoleByName("ROLE_ARTIST").orElse(null));
                artist.setCredentials(credentialsRepository.save(credentials));
                artistRepository.save(artist);
                artistsCount++;
            } catch (Exception ex) {
                log.error("Fake data generation for artist failed", ex);
                artistsCountCatch++;
            }
        }
        return "Generate " + artistsCount + " fake artists\n" +
                "Catch " + artistsCountCatch + " artists\n";
    }
}
