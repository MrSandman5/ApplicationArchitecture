package com.safonov.galleryservice.ArtGalleryApplication.entity.actor;

import com.safonov.galleryservice.ArtGalleryApplication.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@Table(name = "credentials")
@EqualsAndHashCode(callSuper = true)
public final class Credentials extends AbstractEntity {

    @Column(unique = true, name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Email(message = "Invalid email! Please enter valid email")
    @Column(unique = true, name = "email", nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public Credentials(@NotNull final String email,
                       @NotNull final String password,
                       @NotNull final Role role) {
        this.email = email;
        this.password = password;
        this.login = email.split("@")[0];
        this.role = role;
    }

}
