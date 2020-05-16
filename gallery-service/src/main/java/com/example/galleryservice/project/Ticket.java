package com.example.galleryservice.project;

import com.example.galleryservice.user.Client;

import javax.validation.constraints.NotNull;

public class Ticket {
    @NotNull
    private final Client client;
    @NotNull
    private final Expo expo;
    private final Integer cost;

    public Ticket(@NotNull final Client client,
                  @NotNull final Expo expo,
                  final Integer cost) {
        this.client = client;
        this.expo = expo;
        this.cost = cost;
    }

    public Client getClient() {
        return client;
    }

    public Expo getExpo() {
        return expo;
    }

    public Integer getCost() {
        return cost;
    }

}
