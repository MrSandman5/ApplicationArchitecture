package com.safonov.galleryservice.ArtGalleryApplication.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
class ApiValidationError extends ApiSubError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    ApiValidationError(@NotNull final String object,
                       @NotNull final String message) {
        this.object = object;
        this.message = message;
    }
}