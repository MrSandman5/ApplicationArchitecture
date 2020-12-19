package com.safonov.galleryservice.ArtGalleryApplication.model.info;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CredentialsModel {
    private String email;
    private String password;
}
