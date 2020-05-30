package com.example.galleryservice.repository;

import com.example.galleryservice.model.user.Artist;

import javax.transaction.Transactional;

@Transactional
public interface ArtistRepository extends UserRepository<Artist> {
}
