package com.example.galleryservice.model.user;

import com.example.galleryservice.configuration.SpringContext;
import com.example.galleryservice.data.StorageDAO;
import com.example.galleryservice.exceptions.IncorrectPasswordException;
import com.example.galleryservice.exceptions.NotAuthenticatedException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class User {

    private long id;
    private String login;
    private String password;
    private String name;
    private String email;
    private Boolean authentication;
    private UserRole role;

    private StorageDAO storageDAO = SpringContext.getBean(StorageDAO.class);

    public User(@NotNull final String login,
                @NotNull final String password,
                @NotNull final String name,
                @NotNull final String email,
                @NotNull final UserRole role) {
        this.login = login;
        this.name = name;
        this.password = password;
        this.email = email;
        this.authentication = false;
        this.role = role;
    }

    public User(@NotNull final User user){
        this.id = user.id;
        this.login = user.login;
        this.name = user.name;
        this.password = user.password;
        this.email = user.email;
        this.authentication = user.authentication;
        this.role = user.role;
    }

    @SneakyThrows
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
    }

    public boolean isAuthenticated() {
        return authentication;
    }

    public boolean isClient() { return role.equals(UserRole.Client); }
    public boolean isOwner() { return role.equals(UserRole.Owner); }
    public boolean isArtist() { return role.equals(UserRole.Artist); }

    public String toString() {
        return  login + ":" + name + "<" + email + ">";
    }
}
