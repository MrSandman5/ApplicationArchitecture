package com.example.galleryservice.view;

import javax.validation.constraints.NotNull;

public class TicketForm {

    @NotNull
    private String expo;

    public String getExpo() {
        return expo;
    }

    public void setExpo(@NotNull final String expo) {
        this.expo = expo;
    }
}
