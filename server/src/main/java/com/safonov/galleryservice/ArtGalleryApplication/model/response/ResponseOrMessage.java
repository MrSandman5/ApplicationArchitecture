package com.safonov.galleryservice.ArtGalleryApplication.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ResponseOrMessage <T> extends ApiResponse {
    T body;

    public ResponseOrMessage(@NotNull final T body) {
        this.body = body;
    }

    public ResponseOrMessage(@NotNull final String error){
        this.message = error;
    }
}
