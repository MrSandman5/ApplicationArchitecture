package com.safonov.galleryservice.ArtGalleryApplication.data.user;

import com.safonov.galleryservice.ArtGalleryApplication.model.user.Client;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends UserRepository<Client> {
}
