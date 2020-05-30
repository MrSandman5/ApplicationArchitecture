package com.example.galleryservice.repository;

import com.example.galleryservice.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public interface UserRepository<T extends User> extends JpaRepository<T, Long> {
    T findUserByLogin(@NotNull final String name);

    T findUserByEmail(@NotNull final String email);
}
