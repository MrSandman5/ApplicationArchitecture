package com.safonov.galleryservice.ArtGalleryApplication.data.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.configuration.Constants;
import com.safonov.galleryservice.ArtGalleryApplication.data.BaseRepository;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Artist;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Expo;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface ExpoRepository extends BaseRepository<Expo> {

    Optional<Expo> findExpoByName(@NotNull final String name);

    Optional<Expo> findExpoByArtist(@NotNull final Artist artist);

    Optional<Expo> findExpoByStatus(@NotNull final Constants.ExpoStatus status);
}
