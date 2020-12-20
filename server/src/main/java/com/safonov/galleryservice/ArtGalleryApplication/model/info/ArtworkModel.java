package com.safonov.galleryservice.ArtGalleryApplication.model.info;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class ArtworkModel {
    private Long artworkId;
    private String name;
    private String info;
}
