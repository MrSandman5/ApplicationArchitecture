package com.example.galleryservice.user;

import com.example.galleryservice.project.Artwork;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Participant extends User {

    private final List<Artwork> artworks = new ArrayList<>();

    public Participant(final int id,
                       @NotNull final String login,
                       @NotNull final String name,
                       @NotNull final String email) {
        super(id, login, name, email);
    }

    public Participant(@NotNull final User user) {
        super(user);
    }

    public void createListOfArtworks(){

    }

    public void sendArtworks(Owner owner){

    }
}
