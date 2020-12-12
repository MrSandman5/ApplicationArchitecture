package com.safonov.galleryservice.ArtGalleryApplication.data.user;

import com.safonov.galleryservice.ArtGalleryApplication.data.BaseRepository;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.User;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {

    Optional<User> findUserByLogin(@NotNull final String login);

    Optional<User> findUserByName(@NotNull final String name);
}
