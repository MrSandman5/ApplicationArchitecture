package com.example.galleryservice.model.user;

import com.example.galleryservice.exceptions.DBConnectionException;
import com.example.galleryservice.exceptions.IncorrectPasswordException;
import com.example.galleryservice.exceptions.NotAuthenticatedException;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@MappedSuperclass
@Data
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password", nullable = false)
    @Size(min = 8)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    @Size(min = 15)
    private String email;

    @Column(name = "authentication", nullable = false)
    private Boolean authentication;

    public User(@NotNull final String login,
                @NotNull final String password,
                @NotNull final String name,
                @NotNull final String email) {
        this.login = login;
        this.name = name;
        this.name = password;
        this.email = email;
        this.authentication = false;
    }

    public User(@NotNull final User user){
        this.login = user.login;
        this.name = user.name;
        this.password = user.password;
        this.email = user.email;
        this.authentication = user.authentication;
    }

    public void checkAuthentication() throws NotAuthenticatedException{
        if (authentication) return;
        throw new NotAuthenticatedException(toString() + " is not authenticated");
    }

    public Boolean isAuthenticated() {
        return authentication;
    }

    public User getUser(){
        return this;
    }
}
