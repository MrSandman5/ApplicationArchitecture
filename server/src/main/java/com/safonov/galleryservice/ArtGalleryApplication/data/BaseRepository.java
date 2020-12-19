package com.safonov.galleryservice.ArtGalleryApplication.data;

import com.safonov.galleryservice.ArtGalleryApplication.entity.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T extends AbstractEntity> extends JpaRepository<T, Long> {
}
