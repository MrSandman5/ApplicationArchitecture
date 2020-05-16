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

    @NotNull
    private final PrivateAccount privateAccount;
    private final List<Artwork> artworks = new ArrayList<>();

    public Participant(final int id,
                       @NotNull final String login,
                       @NotNull final String name,
                       @NotNull final String email,
                       @NotNull final PrivateAccount privateAccount) {
        super(id, login, name, email);
        this.privateAccount = privateAccount;
    }

    public Participant(@NotNull final User user, @NotNull final PrivateAccount privateAccount) {
        super(user);
        this.privateAccount = privateAccount;
    }

    public void addArtworks(@NotNull final Artwork artwork){
        artworks.add(artwork);
    }

    public List<Artwork> sendArtworks(){
        return artworks;
    }
}
