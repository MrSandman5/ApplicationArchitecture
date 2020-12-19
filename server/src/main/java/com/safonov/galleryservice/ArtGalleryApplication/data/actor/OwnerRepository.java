package com.safonov.galleryservice.ArtGalleryApplication.data.actor;

import com.safonov.galleryservice.ArtGalleryApplication.data.BaseRepository;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Credentials;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Owner;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface OwnerRepository extends BaseRepository<Owner> {
    Optional<Owner> findByCredentials(@NotNull final Credentials credentials);
}
