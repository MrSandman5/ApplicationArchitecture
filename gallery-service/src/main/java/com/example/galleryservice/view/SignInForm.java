package com.example.galleryservice.view;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SignInForm {

    @NotNull
    @Size(min = 5)
    private String login;
    @NotNull
    @Size(min = 6)
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(@NotNull final String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull final String password) {
        this.password = password;
    }
}
