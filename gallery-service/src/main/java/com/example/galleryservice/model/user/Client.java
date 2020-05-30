package com.example.galleryservice.model.user;

import com.example.galleryservice.model.project.Reservation;
import com.example.galleryservice.model.project.Ticket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
public class Client extends User {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clients")
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clients")
    private List<Ticket> tickets = new ArrayList<>();

    public Client(@NotNull final String login,
                  @NotNull final String password,
                  @NotNull final String name,
                  @NotNull final String email) {
        super(login, password, name, email);
    }

    public Client(@NotNull final User user) {
        super(user);
    }
}
