package com.example.galleryservice.view;

import javax.validation.constraints.NotNull;

public class ArtworkForm {

    @NotNull
    private String name;
    @NotNull
    private String info;

    public String getName() {
        return name;
    }

    public void setName(@NotNull final String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(@NotNull final String info) {
        this.info = info;
    }
}
