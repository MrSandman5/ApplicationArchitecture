package com.example.galleryservice.repository;

import com.example.galleryservice.model.user.Client;

import javax.transaction.Transactional;

@Transactional
public interface ClientRepository extends UserRepository<Client> {
}
