package com.safonov.galleryservice.ArtGalleryApplication.data.actor;

import com.safonov.galleryservice.ArtGalleryApplication.data.BaseRepository;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Credentials;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface CredentialsRepository extends BaseRepository<Credentials> {

    Boolean existsCredentialsByEmail(@NotNull final String email);

    Boolean existsCredentialsByLogin(@NotNull final String login);

    Optional<Credentials> findByEmailAndPassword(@NotNull final String email,
                                                 @NotNull final String password);

    Optional<Credentials> findByEmail(@NotNull final String email);

    Optional<Credentials> findByLogin(@NotNull final String login);

    @Override
    void delete(@NotNull final Credentials credentials);
}
