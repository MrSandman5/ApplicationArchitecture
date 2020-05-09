package com.example.galleryservice.project;

import com.example.galleryservice.user.Administrator;
import com.example.galleryservice.user.Client;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class Ticket {
    @NotNull
    private final Client client;
    @NotNull
    private final Administrator admin;
    @NotNull
    private final Expo expo;
    private final Integer cost;
    @NotNull
    private final LocalDate date;

    public Ticket(@NotNull final Client client,
                  @NotNull final Administrator admin,
                  @NotNull final Expo expo,
                  final Integer cost,
                  @NotNull final LocalDate date) {
        this.client = client;
        this.admin = admin;
        this.expo = expo;
        this.cost = cost;
        this.date = date;
    }

    public Client getClient() {
        return client;
    }

    public Administrator getAdmin() {
        return admin;
    }

    public Expo getExpo() {
        return expo;
    }

    public Integer getCost() {
        return cost;
    }

    public LocalDate getDate() {
        return date;
    }

}
