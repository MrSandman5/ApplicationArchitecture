package com.example.galleryservice.view;

import javax.validation.constraints.NotNull;

public class EditExpoForm {

    @NotNull
    private String expo;
    @NotNull
    private String setting;
    @NotNull
    private String data;

    public String getExpo() {
        return expo;
    }

    public void setExpo(@NotNull final String expo) {
        this.expo = expo;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(@NotNull final String setting) {
        this.setting = setting;
    }

    public String getData() {
        return data;
    }

    public void setData(@NotNull final String data) {
        this.data = data;
    }
}
