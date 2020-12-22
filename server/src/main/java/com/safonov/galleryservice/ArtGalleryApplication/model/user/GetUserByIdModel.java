package com.safonov.galleryservice.ArtGalleryApplication.model.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class GetUserByIdModel {
    private Long userId;
    private String userType;
}
