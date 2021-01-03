package com.safonov.galleryservice.ArtGalleryApplication.entity.actor;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.safonov.galleryservice.ArtGalleryApplication.entity.AbstractEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "credentials")
@EqualsAndHashCode(callSuper = true)
public final class Credentials extends AbstractEntity {

    @Column(unique = true, name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(unique = true, name = "email", nullable = false)
    @Pattern(regexp = "^([^ @])+@([^ \\.@]+\\.)+([^ \\.@])+$")
    private String email;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public Credentials(@NotNull final String email,
                       @NotNull final String password) {
        this.email = email;
        this.password = password;
        this.login = email.split("@")[0];
    }

    public Credentials(@NotNull final String login,
                       @NotNull final String password,
                       @NotNull final String email) {
        this.login = login;
        this.email = email;
        this.password = password;
    }

}
