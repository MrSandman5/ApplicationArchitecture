package com.example.galleryservice.service;

import com.example.galleryservice.model.project.Artwork;
import com.example.galleryservice.model.user.User;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ArtworkService {

    List<Artwork> getAllArtworks();

    Artwork findArtworkByName(@NotNull final String name);

    List<Artwork> findArtworksByArtist(@NotNull final User user);

    Artwork findArtworkById(final Long id);

    void deleteArtwork(final Long id);

    void deleteArtworkByName(@NotNull final String name);

    void deleteArtworksByArtist(@NotNull final User user);

}
