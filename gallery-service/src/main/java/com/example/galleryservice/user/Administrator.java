package com.example.galleryservice.user;

import com.example.galleryservice.project.Reservation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class Administrator extends User {

    private Integer profit = 0;

    public Administrator(final int id,
                         @NotNull final String login,
                         @NotNull final String name,
                         @NotNull final String email) {
        super(id, login, name, email);
    }

    public Administrator(@NotNull final User user) {
        super(user);
    }

    public void acceptPayment(Reservation reservation){

    }

    public void sendProfit(Owner owner){

    }
}
