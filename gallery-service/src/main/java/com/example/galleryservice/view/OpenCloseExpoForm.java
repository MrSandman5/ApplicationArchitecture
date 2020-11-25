package com.example.galleryservice.view;

import javax.validation.constraints.NotNull;

public class OpenCloseExpoForm {

    @NotNull
    private String expo;

    public String getExpo() {
        return expo;
    }

    public void setExpo(@NotNull String expo) {
        this.expo = expo;
    }
}
