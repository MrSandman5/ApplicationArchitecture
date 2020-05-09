package com.example.galleryservice.user;

import com.example.galleryservice.exceptions.NotAuthenticatedException;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class User implements UserInterface{

    private Integer id;
    @NotNull
    private String login;
    @NotNull
    private String name;
    @NotNull
    private String email;
    private Boolean authentication;

    public User(final Integer id,
                @NotNull final String login,
                @NotNull final String name,
                @NotNull final String email) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.email = email;
        this.authentication = false;
    }

    public User(@NotNull final User user){
        this.id = user.id;
        this.login = user.login;
        this.name = user.name;
        this.email = user.email;
        this.authentication = user.authentication;
    }

    @Override
    public void setID(final int id) {
        this.id = id;
    }

    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public User getUser() {
        return this;
    }

    @Override
    public void checkAuthentication() throws NotAuthenticatedException{
        if (authentication) return;
        throw new NotAuthenticatedException(toString() + " is not authenticated");
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Boolean isAuthenticated() {
        return authentication;
    }

    public void signOut() {
        authentication = false;
    }

    public String toString() {
        return  login + ":" + name + "<" + email + ">";
    }
}
