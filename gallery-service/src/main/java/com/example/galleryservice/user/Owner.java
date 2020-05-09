package com.example.galleryservice.user;

import com.example.galleryservice.project.Expo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Owner extends User {

    private final List<Participant> participants = new ArrayList<>();
    private final List<Expo> expos = new ArrayList<>();
    private Integer bank;

    public Owner(final int id,
                 @NotNull final String login,
                 @NotNull final String name,
                 @NotNull final String email) {
        super(id, login, name, email);
    }

    public Owner(@NotNull final User user) {
        super(user);
    }

    public void createExpo(Participant participant){

    }

    public void sendPayment(Participant participant){

    }

}
