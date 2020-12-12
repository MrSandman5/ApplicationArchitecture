package com.safonov.galleryservice.ArtGalleryApplication.data.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.data.BaseRepository;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.Artwork;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.Artist;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface ArtworkRepository extends BaseRepository<Artwork> {

    Optional<Artwork> findArtworkByName(@NotNull final String name);

    Optional<Artwork> findArtworkByArtist(@NotNull final Artist artist);

}
