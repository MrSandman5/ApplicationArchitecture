package com.example.galleryservice.repository;

import com.example.galleryservice.model.project.Expo;
import com.example.galleryservice.model.project.ExpoStatus;
import com.example.galleryservice.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface ExpoRepository extends JpaRepository<Expo, Long> {

    Expo findExpoByName(@NotNull final String name);

    List<Expo> findExposByArtist(@NotNull final User user);

    List<Expo> findExposByStatus(@NotNull final ExpoStatus status);

    void deleteExpoByName(@NotNull final String name);

    void deleteExposByArtist(@NotNull final User user);

    void deleteExposByStatus(@NotNull final ExpoStatus status);
}
