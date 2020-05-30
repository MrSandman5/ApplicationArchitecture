package com.example.galleryservice.service.impl;

import com.example.galleryservice.model.project.*;
import com.example.galleryservice.model.user.User;
import com.example.galleryservice.repository.ArtworkRepository;
import com.example.galleryservice.repository.ExpoRepository;
import com.example.galleryservice.repository.UserRepository;
import com.example.galleryservice.service.ExpoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Slf4j
public class ExpoServiceImpl implements ExpoService {

    private final ExpoRepository expoRepository;
    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;

    @Autowired
    public ExpoServiceImpl(@NotNull final ExpoRepository expoRepository,
                           @NotNull final UserRepository userRepository,
                           @NotNull final ArtworkRepository artworkRepository) {
        this.expoRepository = expoRepository;
        this.userRepository = userRepository;
        this.artworkRepository = artworkRepository;
    }

    @Override
    public List<Expo> getAllExpos() {
        List<Expo> result = expoRepository.findAll();
        log.info("IN getAllExpos<Expo> - {} expos found", result.size());
        return result;
    }

    @Override
    public List<Expo> findNewExpos() {
        List<Expo> result = expoRepository.findExposByStatus(ExpoStatus.NEW);
        log.info("IN findNewExpos<Expo> - {} new expos found", result.size());
        return result;
    }

    @Override
    public List<Expo> findStartedExpos() {
        List<Expo> result = expoRepository.findExposByStatus(ExpoStatus.STARTED);
        log.info("IN findStartedExpos<Expo> - {} started expos found", result.size());
        return result;
    }

    @Override
    public List<Expo> findClosedExpos() {
        List<Expo> result = expoRepository.findExposByStatus(ExpoStatus.CLOSED);
        log.info("IN findClosedExpos<Expo> - {} closed expos found", result.size());
        return result;
    }

    @Override
    public Expo findExpoByName(@NotNull String name) {
        Expo result = expoRepository.findExpoByName(name);
        log.info("IN findExpoByName<Expo> - expo: found by name: {}", name);
        return result;
    }

    @Override
    public List<Expo> findExposByArtist(@NotNull User user) {
        List<Expo> result = expoRepository.findExposByArtist(user);
        log.info("IN findExposByArtist<Artwork> - {} expos found", result.size());
        return result;
    }

    @Override
    public Expo findExpoById(Long id) {
        Expo result = expoRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findExpoById<Expo> - no expo found by id: {}", id);
            return null;
        }

        log.info("IN findExpoById<Expo> - expo: {} found by id: {}", result, id);
        return result;
    }

    @Override
    public void deleteExpo(Long id) {
        expoRepository.deleteById(id);
        log.info("IN deleteExpo<Expo> - expo with id: {} successfully deleted", id);
    }

    @Override
    public void deleteExpoByName(@NotNull String name) {
        expoRepository.deleteExpoByName(name);
        log.info("IN deleteExpoByName<Expo> - expo with name: {} successfully deleted", name);
    }

    @Override
    public void deleteExposByArtist(@NotNull User user) {
        expoRepository.deleteExposByArtist(user);
        log.info("IN deleteExposByArtist<Expo> - expos with artist: {} successfully deleted", user);
    }

}
