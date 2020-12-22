package com.safonov.galleryservice.ArtGalleryApplication.data.actor;

import com.safonov.galleryservice.ArtGalleryApplication.data.BaseRepository;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Role;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface RoleRepository extends BaseRepository<Role> {
    Optional<Role> findRoleByName(@NotNull final String name);
}
