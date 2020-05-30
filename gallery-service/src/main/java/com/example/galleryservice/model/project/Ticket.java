package com.example.galleryservice.model.project;

import com.example.galleryservice.model.user.Client;
import com.example.galleryservice.model.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "expo_id", nullable = false)
    private Expo expo;

    @Column(name = "cost")
    private Integer cost;

    public Ticket(@NotNull final Client client,
                  @NotNull final Expo expo,
                  final Integer cost) {
        this.client = client;
        this.expo = expo;
        this.cost = cost;
    }

}
