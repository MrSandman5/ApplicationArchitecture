package com.example.galleryservice.user;

import com.example.galleryservice.exceptions.NotAuthenticatedException;

public interface UserInterface {
    void setID(final int id);
    Integer getID();
    User getUser();
    void checkAuthentication() throws NotAuthenticatedException;
}
