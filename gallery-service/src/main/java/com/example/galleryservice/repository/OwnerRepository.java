package com.example.galleryservice.repository;

import com.example.galleryservice.model.user.Owner;

import javax.transaction.Transactional;

@Transactional
public interface OwnerRepository extends UserRepository<Owner> {
}
