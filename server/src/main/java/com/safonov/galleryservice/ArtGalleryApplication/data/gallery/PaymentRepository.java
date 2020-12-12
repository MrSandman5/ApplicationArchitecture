package com.safonov.galleryservice.ArtGalleryApplication.data.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.data.BaseRepository;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.Payment;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository<R extends Payment> extends BaseRepository<R> {
}
