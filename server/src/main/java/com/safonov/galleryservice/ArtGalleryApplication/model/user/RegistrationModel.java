package com.safonov.galleryservice.ArtGalleryApplication.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.CredentialsModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class RegistrationModel extends CredentialsModel {
    @JsonProperty("firstName")
    protected String firstName;

    @JsonProperty("lastname")
    protected String lastName;

    @JsonProperty("role")
    protected String role;
}

