package com.example.galleryservice.service;

import com.example.galleryservice.model.user.User;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface UserService<T extends User> {

    void register(@NotNull final String login,
                  @NotNull final String password,
                  @NotNull final String name,
                  @NotNull final String email);

    void register(@NotNull final T user);

    void signIn(@NotNull final String login,
                      @NotNull final String password);

    void signOut(@NotNull final String login);

    List<T> getAllUsers();

    T findUserByLogin(@NotNull final String login);

    T findUserByEmail(@NotNull final String email);

    T findUserById(final Long id);

    void deleteUser(final Long id);

}
