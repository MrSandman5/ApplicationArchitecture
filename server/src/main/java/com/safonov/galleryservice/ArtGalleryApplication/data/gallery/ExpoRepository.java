package com.safonov.galleryservice.ArtGalleryApplication.data.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.configuration.Constants;
import com.safonov.galleryservice.ArtGalleryApplication.data.BaseRepository;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Artist;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Expo;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpoRepository extends BaseRepository<Expo> {

    Optional<Expo> findExpoByName(@NotNull final String name);

    List<Expo> findExposByArtist(@NotNull final Artist artist);

    List<Expo> findExposByStatus(@NotNull final Constants.ExpoStatus status);

    Optional<Expo> findExpoByArtistAndStatus(@NotNull final Artist artist,
                                             @NotNull final Constants.ExpoStatus status);
}
