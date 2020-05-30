package com.example.galleryservice.service;

import com.example.galleryservice.model.project.Expo;
import com.example.galleryservice.model.user.User;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ExpoService {

    List<Expo> getAllExpos();

    List<Expo> findNewExpos();

    List<Expo> findStartedExpos();

    List<Expo> findClosedExpos();

    Expo findExpoByName(@NotNull final String name);

    List<Expo> findExposByArtist(@NotNull final User user);

    Expo findExpoById(final Long id);

    void deleteExpo(final Long id);

    void deleteExpoByName(@NotNull final String name);

    void deleteExposByArtist(@NotNull final User user);

}
