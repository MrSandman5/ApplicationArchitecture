package com.example.galleryservice.model.gallery;

import javax.validation.constraints.NotNull;

public enum EditSettings {

    Name("Name"), Info("Info"), StartTime("StartTime"), EndTime("EndTime");

    EditSettings(@NotNull final String setting){ }
}
