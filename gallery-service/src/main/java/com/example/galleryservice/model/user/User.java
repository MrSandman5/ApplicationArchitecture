package com.example.galleryservice.model.user;

import com.example.galleryservice.configuration.SpringContext;
import com.example.galleryservice.data.StorageDAO;
import com.example.galleryservice.exceptions.IncorrectPasswordException;
import com.example.galleryservice.exceptions.NotAuthenticatedException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class User {

    private long id;
    @NotNull
    @Size(min = 5)
    private String login;
    @NotNull
    @Size(min = 6)
    private String password;
    @NotNull
    private String name;
    @NotNull
    @Email(message = "Invalid email! Please enter valid email")
    private String email;
    @NotNull
    private Boolean authentication;

    private final StorageDAO storageDAO = SpringContext.getBean(StorageDAO.class);

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

    public String toString() {
        return  login + ":" + name + "<" + email + ">";
    }

}
