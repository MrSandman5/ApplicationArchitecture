package com.example.galleryservice.data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    Optional<T> findByID(final long id);
    List<T> findAll();
    void update(@NotNull final T item);
    long insert(@NotNull final T item);
}
