package com.example.galleryservice.model.user;

import javax.validation.constraints.NotNull;

public enum UserRole {

    Client("Client"), Owner("Owner"), Artist("Artist");

    UserRole(@NotNull final String role){ }

}
