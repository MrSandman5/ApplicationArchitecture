package com.example.galleryservice.model.gallery;

import javax.validation.constraints.NotNull;

public enum ExpoStatus {

    New("New"), Opened("Opened"), Closed("Closed");

    ExpoStatus(@NotNull final String status){ }

}
