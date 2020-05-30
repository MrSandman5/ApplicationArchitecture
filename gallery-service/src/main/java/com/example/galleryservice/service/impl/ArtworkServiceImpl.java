package com.example.galleryservice.service.impl;

import com.example.galleryservice.model.project.Artwork;
import com.example.galleryservice.model.user.User;
import com.example.galleryservice.repository.ArtworkRepository;
import com.example.galleryservice.repository.UserRepository;
import com.example.galleryservice.service.ArtworkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Slf4j
public class ArtworkServiceImpl implements ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;

    @Autowired
    public ArtworkServiceImpl(@NotNull final ArtworkRepository artworkRepository,
                              @NotNull final UserRepository userRepository) {
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Artwork> getAllArtworks() {
        List<Artwork> result = artworkRepository.findAll();
        log.info("IN getAllArtworks<Artwork> - {} artworks found", result.size());
        return result;
    }

    @Override
    public Artwork findArtworkByName(@NotNull String name) {
        Artwork result = artworkRepository.findArtworkByName(name);
        log.info("IN findArtworkByName<Artwork> - artwork: found by name: {}", name);
        return result;
    }

    @Override
    public List<Artwork> findArtworksByArtist(@NotNull User user) {
        List<Artwork> result = artworkRepository.findArtworksByArtist(user);
        log.info("IN findArtworksByArtist<Artwork> - {} artworks found", result.size());
        return result;
    }

    @Override
    public Artwork findArtworkById(Long id) {
        Artwork result = artworkRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findArtworkById<Artwork> - no artwork found by id: {}", id);
            return null;
        }

        log.info("IN findArtworkById<Artwork> - artwork: {} found by id: {}", result, id);
        return result;
    }

    @Override
    public void deleteArtwork(Long id) {
        artworkRepository.deleteById(id);
        log.info("IN deleteArtwork<Artwork> - artwork with id: {} successfully deleted", id);
    }

    @Override
    public void deleteArtworkByName(@NotNull String name) {
        artworkRepository.deleteArtworkByName(name);
        log.info("IN deleteArtworkByName<Artwork> - artwork with name: {} successfully deleted", name);
    }

    @Override
    public void deleteArtworksByArtist(@NotNull User user) {
        artworkRepository.deleteArtworksByArtist(user);
        log.info("IN deleteArtworkByName<Artwork> - artworks with artist: {} successfully deleted", user);
    }
}
