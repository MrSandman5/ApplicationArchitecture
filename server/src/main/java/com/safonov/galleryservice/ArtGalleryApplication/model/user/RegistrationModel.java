package com.safonov.galleryservice.ArtGalleryApplication.model.user;

import com.safonov.galleryservice.ArtGalleryApplication.configuration.Constants;
import com.safonov.galleryservice.ArtGalleryApplication.model.info.CredentialsModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class RegistrationModel extends CredentialsModel {
    protected String firstName;
    protected String lastName;
    protected Constants.Roles role;
}

