package com.safonov.galleryservice.ArtGalleryApplication.data.user;

import com.safonov.galleryservice.ArtGalleryApplication.model.user.Artist;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends UserRepository<Artist> {
}
