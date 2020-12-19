package com.safonov.galleryservice.ArtGalleryApplication.model.user;

import com.safonov.galleryservice.ArtGalleryApplication.configuration.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class IdAndPersonTypeModel {
    Long personId;
    Constants.UserType personType;
}
