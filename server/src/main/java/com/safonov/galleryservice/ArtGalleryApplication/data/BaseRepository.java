package com.safonov.galleryservice.ArtGalleryApplication.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Long> {

    Optional<T> findByID(final long id);

    List<T> findAll();

    void update(@NotNull final T item);
    long insert(@NotNull final T item);
}
