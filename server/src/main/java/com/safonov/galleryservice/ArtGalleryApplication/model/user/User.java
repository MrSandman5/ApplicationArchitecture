package com.safonov.galleryservice.ArtGalleryApplication.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @NotNull
    @Size(min = 10)
    @Column(name = "login")
    private String login;

    @NotNull
    @Size(min = 18)
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Email(message = "Invalid email! Please enter valid email")
    @Column(name = "email")
    private String email;

    @NotNull
    private Boolean authentication;

    //private final StorageDAO storageDAO = SpringContext.getBean(StorageDAO.class);

    public User(@NotNull final String login,
                @NotNull final String password,
                @NotNull final String name,
                @NotNull final String email) {
        this.login = login;
        this.name = name;
        this.password = password;
        this.email = email;
        this.authentication = false;
    }

    public User(@NotNull final User user){
        this.id = user.id;
        this.login = user.login;
        this.name = user.name;
        this.password = user.password;
        this.email = user.email;
        this.authentication = user.authentication;
    }

    /*@SneakyThrows
    public void signIn(@NotNull final String password) {
        if (!this.password.equals(password)){
            throw new IncorrectPasswordException();
        }
        authentication = true;
    }

    public void signOut() {
        authentication = false;
    }

    public void checkAuthentication() throws NotAuthenticatedException{
        if (isAuthenticated()) return;
        throw new NotAuthenticatedException(toString() + " is not authenticated");
    }*/

    public boolean isAuthenticated() {
        return authentication;
    }

    public String toString() {
        return  login + ":" + name + "<" + email + ">";
    }
}
