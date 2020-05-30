package com.example.galleryservice.model.user;

import com.example.galleryservice.model.project.Artwork;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "artists")
@Data
public class Artist extends User {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "artists")
    private final List<Artwork> artworks = new ArrayList<>();

    public Artist(@NotNull final String login,
                  @NotNull final String password,
                  @NotNull final String name,
                  @NotNull final String email) {
        super(login, password, name, email);
    }

    public Artist(@NotNull final User user) {
        super(user);
    }

}
