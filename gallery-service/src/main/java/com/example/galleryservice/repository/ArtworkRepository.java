package com.example.galleryservice.repository;

import com.example.galleryservice.model.project.Artwork;
import com.example.galleryservice.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

    Artwork findArtworkByName(@NotNull final String name);

    List<Artwork> findArtworksByArtist(@NotNull final User user);

    void deleteArtworkByName(@NotNull final String name);

    void deleteArtworksByArtist(@NotNull final User user);
}
