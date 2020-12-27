package com.safonov.galleryservice.ArtGalleryApplication.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.CredentialsModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RegistrationModel extends CredentialsModel {
    @NotBlank
    @JsonProperty("firstName")
    protected String firstName;

    @NotBlank
    @JsonProperty("lastname")
    protected String lastName;

    @JsonProperty("roles")
    protected Set<String> roles;
}

