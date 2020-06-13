package com.example.galleryservice.model.gallery;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OwnerArtistPayment extends Payment {

    private Long expo;
    private Long owner;
    private Long artist;

    public OwnerArtistPayment(final Long expo,
                              final Long owner,
                              final Long artist,
                              final Double amount) {
        super(amount);
        this.expo = expo;
        this.owner = owner;
        this.artist = artist;
    }
}
